import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Set<T> implements Serializable, Iterable<T> {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public Set() {
    elements = new ArrayList<T>();
  }

  public Set(Set<T> other) {
    elements = new ArrayList<T>();
    elements.addAll(other.elements);
  }

  public Set<T> append(T object) {
    elements.add(object);
    return this;
  }

  public Set<T> union(Set<T> that) {
    elements.addAll(that.elements);
    return this;
  }

  public Set<T> topK(int k) {
    assert (k > 0 && k <= size());
    Set<T> topKSet = new Set<T>();
    for (T elem : elements) {
      if (--k < 0) {
        break;
      }
      topKSet.append(elem);
    }
    return topKSet;
  }

  public Set<T> reverseTopK(int k) {
    assert (k > 0 && k <= size());
    Set<T> topKSet = new Set<T>();
    int elementSize = elements.size();
    for (int i = elementSize - 1; i >= elementSize - k; --i) {
      topKSet.append(elements.get(i));
    }
    return topKSet;
  }

  @Override
  public Iterator<T> iterator() {
    return this.elements.iterator();
  }

  int size() {
    return elements.size();
  }

  boolean isEmpty() {
    return elements.isEmpty();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    int count = 0, size = elements.size();
    sb.append(StringUtil.ARRAY_START);
    for (T elem : elements) {
      sb.append(elem);
      if (++count != size) {
        sb.append(StringUtil.ARRAY_SPLITTER);
      }
    }
    sb.append(StringUtil.ARRAY_END);
    return sb.toString();
  }

  protected List<T> elements;

}
