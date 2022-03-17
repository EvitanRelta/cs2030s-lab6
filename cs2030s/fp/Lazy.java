package cs2030s.fp;

/**
 * For lazy evaluation of values that are 
 * are expensive to produce.
 *
 * @author Tan Zong Zhi, Shaun (Group 16A)
 * @version CS2030S AY 21/22 Sem 2 
 *
 * @param <T> The type of the produced value, returned by the 'get' method.
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

  public Lazy<Boolean> filter(BooleanCondition<? super T> predicate) {
    Producer<Boolean> newProducer = () -> predicate.test(this.get());
    return Lazy.of(newProducer);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Lazy<?>)) {
      return false;
    }

    Lazy<?> lazyObj = (Lazy<?>) obj;

    // Ensure both Lazy instanced are evaluated
    this.get();
    lazyObj.get();

    return this.value
        .equals(lazyObj.value);
  }

  public <S, R> Lazy<R> combine(Lazy<S> lazyObj, 
      Combiner<? super T, ? super S, ? extends R> combiner) {
    Producer<R> newProducer = () -> combiner.combine(this.get(), lazyObj.get());
    return Lazy.of(newProducer);
  }
}
