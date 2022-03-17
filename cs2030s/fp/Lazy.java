package cs2030s.fp;

public class Lazy<T> {
  private Producer<T> producer;
  private Maybe<T> value;
}
