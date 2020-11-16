import org.mangosdk.spi.ProviderFor;

@ProviderFor(Comparable.class)
public class GenericsTestClass implements Comparable<Integer> {

	@Override
	public int compareTo(Integer o) {
		return 0;
	}
}
