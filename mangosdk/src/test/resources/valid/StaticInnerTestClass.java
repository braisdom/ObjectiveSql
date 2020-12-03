import java.util.RandomAccess;

import org.mangosdk.spi.ProviderFor;

public class StaticInnerTestClass {
	@ProviderFor(RandomAccess.class)
	public static class Inner implements RandomAccess {
		
	}
}
