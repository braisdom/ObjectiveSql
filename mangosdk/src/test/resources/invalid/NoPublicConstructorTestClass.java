import java.util.RandomAccess;

import org.mangosdk.spi.ProviderFor;

@ProviderFor(RandomAccess.class)
public class NoPublicConstructorTestClass implements RandomAccess {
	private NoPublicConstructorTestClass() {
	}
}
