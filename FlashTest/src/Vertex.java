import java.util.NoSuchElementException;

public class Vertex<T> extends Attribute {

  /**
   * 
   */
  private static final long serialVersionUID = 2573902725010692564L;
  
  public Vertex(T id) {
    this.id = id;
  }

  public T id() {
    return this.id;
  }
  
  public void setId(T id) {
    this.id = id;
  }

  public AttributeSet<Edge<T>> in() {
    return this.in;
  }

  public AttributeSet<Edge<T>> out() {
    return this.out;
  }

  public AttributeSet<Edge<T>> neighbors() {
    Set<Edge<T>> neighbor = new Set<Edge<T>>(this.in);
    return (AttributeSet<Edge<T>>)neighbor.union(out);
  }

  public <VT> VT getAttribute(String attrName, Class<VT> attrClass) {
    return this.get(Attribute.key(attrName, attrClass));
  }

  public <VT> VT getTempAttribute(String attrName, Class<VT> attrClass) {
    return tempAttribute.get(Attribute.key(attrName, attrClass));
  }

  /**
   * Make the temporary attribute specified by key permanent attribute
   * 
   * @param key
   * @return
   * @throws Exception
   */
  public <VT> void makeTempPermanant(Key<VT> key) throws Exception {
    VT value = tempAttribute.remove(key);
    if (value == null) {
      throw new NoSuchElementException(
          "Key: " + key + " is not present in the temporary Attribute.");
    }
    if (this.hasAttribute(key)) {
      throw new Exception("Key: " + key + " is already present in the Attribute.");
    }
    set(key, value);
  }

  private Attribute tempAttribute;
  private AttributeSet<Edge<T>> in;
  private AttributeSet<Edge<T>> out;
  private T id;
}
