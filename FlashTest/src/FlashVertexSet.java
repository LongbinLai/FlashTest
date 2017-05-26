
public interface FlashVertexSet {


  public <T extends Comparable<T>> FlashVertexSet filter(Attribute.Predicate<T> predicate);

  // local(x = function())
  public <T> FlashVertexSet local(String newKey, LocalFunction<T> function) throws Exception;

  public <T> FlashVertexSet append(T object);

  @Override
  public String toString();
}
