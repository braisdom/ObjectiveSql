/*

Copyright 2008 TOPdesk, the Netherlands

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

*/

package org.mangosdk.spi.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Types;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.tools.Diagnostic.Kind;

import org.mangosdk.spi.ProviderFor;

@SupportedAnnotationTypes("*")
@SupportedOptions({Options.SPI_DIR_OPTION, Options.SPI_LOG_OPTION, Options.SPI_VERBOSE_OPTION, Options.SPI_DISABLED_OPTION})
public class SpiProcessor extends AbstractProcessor {
	
	public static final String NAME = SpiProcessor.class.getName() 
		+ " (" + SpiProcessor.class.getPackage().getImplementationVersion() + ")";

	private static final Pattern RELEASE_PATTERN = Pattern.compile("^RELEASE_(\\d+)$");
	private static final int MAX_SUPPORTED_VERSION = 8;

	private Options options;
	private Logger logger;
	private Persistence persistence;
	private Collector data;

	@Override
	public SourceVersion getSupportedSourceVersion() {
		SourceVersion[] svs = SourceVersion.values();
		for (int i = svs.length - 1; i >= 0; i--) {
			String name = svs[i].name();
			Matcher m = RELEASE_PATTERN.matcher(name);
			if (m.matches()) {
				int release = Integer.parseInt(m.group(1));
				if (release <= MAX_SUPPORTED_VERSION) return svs[i];
			}
		}

		return SourceVersion.RELEASE_6;
	}

	@Override
	public synchronized void init(ProcessingEnvironment environment) {
		super.init(environment);
		try {
			initialize();
		}
		catch (Exception e) {
			environment.getMessager().printMessage(Kind.ERROR, ProcessorLogger.exceptionToString(e));
		}
	}

	private void initialize() {
		options = new Options(processingEnv.getOptions());
		if (options.disabled()) {
			return;
		}
		logger = new ProcessorLogger(processingEnv.getMessager(), options);
		
		checkCompatibility();
		
		persistence = new Persistence(NAME, options.dir(), processingEnv.getFiler(), logger);
		data = new Collector(persistence.getInitializer(), logger);
		
		// Initialize if possible 
		for (String serviceName : persistence.tryFind()) {
			data.getService(serviceName);
		}
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if (options.disabled()) {
			return false;
		}
		
		long start = System.currentTimeMillis();
		logger.note(LogLocation.LOG_FILE, "Starting round with " + roundEnv.getRootElements().size() + " elements");
		
		removeStaleData(roundEnv);
		
		handleAnnotations(roundEnv);
		
		long end = System.currentTimeMillis();
		logger.note(LogLocation.LOG_FILE, "Ending round in " + (end - start) + " milliseconds");
		if (roundEnv.processingOver()) {
			writeData();
		}
		return false;
	}

	private void writeData() {
		logger.note(LogLocation.LOG_FILE, "Writing output");
		for (Service service : data.services()) {
			try {
				persistence.write(service.getName(), service.toProviderNamesList());
			} 
			catch (IOException e) {
				processingEnv.getMessager().printMessage(Kind.ERROR, e.getMessage());
			}
		}
		persistence.writeLog();
	}

	private void removeStaleData(RoundEnvironment roundEnv) {
		for (Element e : roundEnv.getRootElements()) {
			if (e instanceof TypeElement) {
				TypeElement currentClass = (TypeElement)e;
				data.removeProvider(createProperQualifiedName(currentClass));
			}
		}
	}

	private void handleAnnotations(RoundEnvironment roundEnv) {
		Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ProviderFor.class);
		for (Element e : elements) {
			handleElement(e);
		}
	}

	private void handleElement(Element e) {
		TypeElement currentClass = (TypeElement)e;
		
		CheckResult checkResult = checkCurrentClass(currentClass);
		if (checkResult.isError()) {
			reportError(currentClass, checkResult);
			return;
		}
		
		for (TypeElement service : findServices(currentClass)) {
			CheckResult implementationResult = isImplementation(currentClass, service);
			if (implementationResult.isError()) {
				reportError(currentClass, implementationResult);
			}
			else {
				register(createProperQualifiedName(service), currentClass);
			}
		}
	}
	
	private void reportError(TypeElement element, CheckResult result) {
		processingEnv.getMessager().printMessage(Kind.ERROR, element.getSimpleName() + " " + result.getMessage(), element);
	}

	private CheckResult checkCurrentClass(TypeElement currentClass) {
		if (currentClass.getKind() != ElementKind.CLASS) {
			return CheckResult.valueOf("is not a class");
		}
		
		if (!currentClass.getModifiers().contains(Modifier.PUBLIC)) {
			return CheckResult.valueOf("is not a public class");
		}
		
		if (!isStaticClass(currentClass)) {
			return CheckResult.valueOf("is not a static class");
		}
		
		if (!hasCorrectConstructor(currentClass)) {
			return CheckResult.valueOf("has no public no-args constructor");
		}
		
		return CheckResult.OK;
	}

	private boolean hasCorrectConstructor(TypeElement currentClass) {
		List<ExecutableElement> constructors = ElementFilter.constructorsIn(currentClass.getEnclosedElements());
		for (ExecutableElement constructor : constructors) {
			if (constructor.getModifiers().contains(Modifier.PUBLIC) && constructor.getParameters().isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isStaticClass(TypeElement element) {
		if (element.getEnclosingElement().getKind() != ElementKind.CLASS) {
			return true;
		}
		return element.getModifiers().contains(Modifier.STATIC);
	}

	private CheckResult isImplementation(TypeElement currentClass, TypeElement provider) {
		if (isAssignable(currentClass.asType(), provider.asType())) {
			return CheckResult.OK;
		}
		
		String message;
		if (provider.getKind() == ElementKind.INTERFACE) {
			message = "does not implement";
		}
		else {
			message = "does not extend";
		}
		return CheckResult.valueOf(message + " " + provider.getQualifiedName());
		
	}
	
	private boolean isAssignable(TypeMirror currentClass, TypeMirror provider) {
		Types typeUtils = processingEnv.getTypeUtils();
		if (typeUtils.isAssignable(typeUtils.erasure(currentClass), typeUtils.erasure(provider))) {
			return true;
		}

		for (TypeMirror superType : typeUtils.directSupertypes(currentClass)) {
			if (isAssignable(superType, provider)) {
				return true;
			}
		}
		return false;
	}
	

	private List<TypeElement> findServices(TypeElement classElement) {
		List<TypeElement> services = new ArrayList<TypeElement>();
		
		for (AnnotationMirror annotation : findAnnotationMirrors(classElement, ProviderFor.class.getName())) {
			for (AnnotationValue value : findValue(annotation)) {
				services.add(toElement(value));
			}
		}
		
		return services;
	}

	private static List<AnnotationMirror> findAnnotationMirrors(TypeElement element, String lookingFor) {
		List<AnnotationMirror> annotationMirrors = new ArrayList<AnnotationMirror>();
		for (AnnotationMirror annotation : element.getAnnotationMirrors()) {
			if (annotationMirrorMatches(annotation, lookingFor)) {
				annotationMirrors.add(annotation);
			}
		}
		return annotationMirrors;
	}

	private static boolean annotationMirrorMatches(AnnotationMirror annotation, String lookingFor) {
		Name qualifiedName = ((TypeElement)(annotation.getAnnotationType()).asElement()).getQualifiedName();
		return qualifiedName.contentEquals(lookingFor);
	}

	private TypeElement toElement(AnnotationValue value) {
		return (TypeElement)((DeclaredType)((TypeMirror)value.getValue())).asElement();
	}

	private Collection<AnnotationValue> findValue(AnnotationMirror mirror) {
		Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = mirror.getElementValues();
		for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : elementValues.entrySet()) {
			if (entry.getKey().getSimpleName().contentEquals("value")) {
				@SuppressWarnings("unchecked")
				Collection<AnnotationValue> result = (Collection<AnnotationValue>)entry.getValue().getValue();
				return result;
			}
		}
		throw new IllegalStateException("No value found in element");
	}
	
	private void register(String serviceName, TypeElement provider) {
		data.getService(serviceName).addProvider(createProperQualifiedName(provider));
	}

	private String createProperQualifiedName(TypeElement provider) {
		return processingEnv.getElementUtils().getBinaryName(provider).toString();
	}
	
	private void checkCompatibility() {
		logger.note(LogLocation.MESSAGER, "Testing for compatability options");
		try {
			checkJavacOnLinux();
		}
		catch (Exception e) {
			warning(ProcessorLogger.exceptionToString(e));
		}
		logger.note(LogLocation.MESSAGER, "Testing complete");
	}

	private void checkJavacOnLinux() {
		try {
			FileObject resource = processingEnv.getFiler().getResource(StandardLocation.CLASS_OUTPUT, "", options.dir() + "a/b");
			if (resource.toUri().toString().equals("b")) {
				warning("Output files will be placed in the root of the output folder.\n  This is a known bug in the java compiler on Linux.\n  Please use the -d compiler option to circumvent this problem.\n  See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6647996 for more information.");
			}
		} 
		catch (IOException e) {
			warning("IOException during testing Javac on Linux");
		}
	}
	
	private void warning(String message) {
		processingEnv.getMessager().printMessage(Kind.WARNING, message);
	}
}