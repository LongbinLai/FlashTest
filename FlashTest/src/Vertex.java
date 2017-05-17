
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
  
  public Set<Edge<T>> neighbor() {
    Set<Edge<T>> neighbor = new Set<Edge<T>>(this.in);
    return neighbor.union(out);
  }
  
  public <VT> VT getAttribute(String attrName, Class<VT> attrClass) {
    return this.get(Attributes.key(attrName, attrClass));
  }
  
  public <VT> VT getTempAttribute(String attrName, Class<VT> attrClass) {
    return tempAttributes.get(Attributes.key(attrName, attrClass));
  } 
  
  private Attributes tempAttributes;
  private Set<Edge<T>> in;
  private Set<Edge<T>> out;
  private T id;
}