
public class Edge<T> extends Attributes {

  /**
   * 
   */
  private static final long serialVersionUID = -3260390588366778186L;
  
  public Edge(T src, T dst) {
    super();
    this.srcId = src;
    this.dstId = dst;
  }
  
  public T srcId() {
    return this.srcId;
  }
  
  public T dstId() {
    return this.dstId;
  }
 
  private T srcId;
  private T dstId;
}