package cs2030s.fp;


/**
 * For lazy evaluation of values that are 
 * are expensive to produce.
 * CS2030S Lab 6
 * AY21/22 Semester 2
 *
 * @author Tan Zong Zhi, Shaun (Group 16A)
 *
 * @param <T> The type of the produced value
 */
public class Lazy<T> {
  private Producer<? extends T> producer;
  private Maybe<T> value;

  private Lazy(T value) {
    this.value = Maybe.some(value);
  }

  private Lazy(Producer<? extends T> producer) {
    this.producer = producer;
  }

  public static <T> Lazy<T> of(T v) {
    return new Lazy<>(v);
  }

  public static <T> Lazy<T> of(Producer<? extends T> s) {
    return new Lazy<>(s);
  }

  public T get() {
    if (this.value == null) {
      T rawValue = this.producer.produce();
      this.value = Maybe.some(rawValue);
    }
    return this.value.get();
  }

  @Override
  public String toString() {
    return this.value == null
        ? "?"
        : String.format("%s", this.get());
  }

  public <U> Lazy<U> map(Transformer<? super T, ? extends U> transformer) {
    Producer<U> newProducer = () -> transformer
        .transform(this.get());
    return Lazy.of(newProducer);
  }

  public <U> Lazy<U> flatMap(Transformer<? super T, 
        ? extends Lazy<? extends U>> transformer) {
    Producer<U> newProducer = () -> transformer
        .transform(this.get())
        .get();
    return Lazy.of(newProducer);
  }
}
