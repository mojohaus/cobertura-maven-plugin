import java.util.Arrays;
import java.util.List;

public class Foo {
  private final List<String> list = Arrays.asList("A", "B", "C");

  public void bar() {
    list.stream().forEach(x -> System.out.println(x));
  }
}
