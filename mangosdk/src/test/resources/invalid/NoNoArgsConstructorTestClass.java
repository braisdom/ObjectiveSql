import java.util.RandomAccess;

import org.mangosdk.spi.ProviderFor;

@ProviderFor(RandomAccess.class)
public class NoNoArgsConstructorTestClass implements RandomAccess {
	public NoNoArgsConstructorTestClass(String name) {
	}
}
