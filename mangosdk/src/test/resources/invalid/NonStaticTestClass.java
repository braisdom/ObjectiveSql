import java.util.RandomAccess;

import org.mangosdk.spi.ProviderFor;

public class NonStaticTestClass {

	@ProviderFor(RandomAccess.class)
	public class InnerNonStaticTestClass {
		String value();
	}
}
