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
    this.value = Maybe.none();
  }

  public static <T> Lazy<T> of(T v) {
    return new Lazy<>(v);
  }

  public static <T> Lazy<T> of(Producer<? extends T> s) {
    return new Lazy<>(s);
  }

  public T get() {
    // 'wrapper' is only called when 'this.value == Maybe.none()'.
    // Thus, 'this.value' is only reassigned once when 'get' is called
    // multiple times.
    Producer<T> wrapper = () -> {
      T rawValue = this.producer.produce();
      this.value = Maybe.some(rawValue);
      return rawValue;
    };
    return this.value
        .orElseGet(wrapper);
  }

  @Override
  public String toString() {
    return this.value
        .map(String::valueOf)
        .orElse("?");
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
