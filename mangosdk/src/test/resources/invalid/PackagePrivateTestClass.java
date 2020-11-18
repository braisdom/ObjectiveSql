import java.util.RandomAccess;

import org.mangosdk.spi.ProviderFor;

@ProviderFor(RandomAccess.class)
class PackagePrivateTestClass {
	String value();
}
