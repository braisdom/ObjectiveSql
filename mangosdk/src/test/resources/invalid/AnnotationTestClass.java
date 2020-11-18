import java.util.RandomAccess;

import org.mangosdk.spi.ProviderFor;

@ProviderFor(RandomAccess.class)
public @interface AnnotationTestClass {
	String value();
}
