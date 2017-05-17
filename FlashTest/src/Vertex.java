import java.util.NoSuchElementException;

public class Vertex<T> extends Attributes {

  /**
   * 
   */
  private static final long serialVersionUID = 2573902725010692564L;

  public T id() {
    return this.id;
  }

  public Set<Edge<T>> in() {
    return this.in;
  }

  public Set<Edge<T>> out() {
    return this.out;
  }

  public Set<Edge<T>> neighbors() {
    Set<Edge<T>> neighbor = new Set<Edge<T>>(this.in);
    return neighbor.union(out);
  }

  public <VT> VT getAttribute(String attrName, Class<VT> attrClass) {
    return this.get(Attributes.key(attrName, attrClass));
  }

  public <VT> VT getTempAttribute(String attrName, Class<VT> attrClass) {
    return tempAttributes.get(Attributes.key(attrName, attrClass));
  }

  /**
   * Make the temporary attribute specified by key permanent attribute
   * 
   * @param key
   * @return
   * @throws Exception
   */
  public <VT> void makeTempPermanant(Key<VT> key) throws Exception {
    VT value = tempAttributes.remove(key);
    if (value == null) {
      throw new NoSuchElementException(
          "Key: " + key + " is not present in the temporary attributes.");
    }
    if (this.hasAttribute(key)) {
      throw new Exception("Key: " + key + " is already present in the attributes.");
    }
    set(key, value);
  }

  private Attributes tempAttributes;
  private Set<Edge<T>> in;
  private Set<Edge<T>> out;
  private T id;
}
