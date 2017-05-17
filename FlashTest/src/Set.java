import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Set<T extends Attributes> implements Serializable, Iterable<T> {

  // TODO(longbin) Should create some test cases.
  public static void main(String[] args) {
    Set<Attributes> set = new Set<Attributes>();
    Attributes attr1 = new Attributes();
    attr1.set(Attributes.key("age", Integer.class), 5);
    Attributes attr2 = new Attributes();
    attr2.set(Attributes.key("age", Integer.class), 1);
    Attributes attr3 = new Attributes();
    attr3.set(Attributes.key("age", Integer.class), 3);
    set.append(attr1).append(attr2).append(attr3);

    Set<Attributes> filtered = set.filter(
        new Predicator<>(Attributes.key("age", Integer.class), CompareUtil.BiOprator.LT, 4));
    System.out.println(set);
    System.out.println(filtered);

    try {
      System.out
          .println(set.aggregation(Attributes.key("age", Integer.class), AggregationType.SUM));
      System.out
          .println(set.aggregation(Attributes.key("age", Integer.class), AggregationType.MAX));
      System.out
          .println(set.aggregation(Attributes.key("age", Integer.class), AggregationType.MIN));
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

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

  public <VT extends Comparable<VT>> Set<T> filter(Predicator<VT> predicator) {
    Set<T> filtered = new Set<>();
    for (T elem : elements) {
      if (elem.on(predicator)) {
        filtered.append(elem);
      }
    }
    return filtered;
  }

  public <VT extends Comparable<VT>> Set<T> filter(List<Predicator<VT>> predicators) {
    Set<T> filtered = new Set<>();
    for (T elem : elements) {
      boolean allGood = true;
      for (Predicator<VT> pred : predicators) {
        allGood = elem.on(pred);
        if (!allGood) {
          break;
        }
      }
      if (allGood) {
        filtered.append(elem);
      }
    }
    return filtered;
  }

  @SuppressWarnings("unchecked")
  public <VT extends Comparable<VT>> VT aggregation(Attributes.Key<VT> key, AggregationType agg)
      throws Exception {

    switch (agg) {
      case SUM:
        if (key.type() == Integer.class) {
          return (VT) this.sum((Attributes.Key<Integer>) key, new IntegerAdder());
        } else if (key.type() == Long.class) {
          return (VT) this.sum((Attributes.Key<Long>) key, new LongAdder());
        } else if (key.type() == Float.class) {
          return (VT) this.sum((Attributes.Key<Float>) key, new FloatAdder());
        } else if (key.type() == Double.class) {
          return (VT) this.sum((Attributes.Key<Double>) key, new DoubleAdder());
        } else {
          throw new Exception("The type does not support SUM.");
        }
      case MAX:
        return this.max(key);
      case MIN:
        return this.min(key);
      default:
        throw new Exception("Aggregation function not supported.");
    }
  }

  public <VT extends Comparable<VT>> VT aggregation(Attributes.Key<VT> key, AggregationType agg,
      List<Predicator<VT>> predicators) throws Exception {
    Set<T> filtered = this.filter(predicators);
    return filtered.aggregation(key, agg);
  }
  
  public Set<T> union(Set<T> that) {
    elements.addAll(that.elements);
    return this;
  }

  @Override
  public Iterator<T> iterator() {
    return this.elements.iterator();
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

  private <VT extends Number> VT sum(Attributes.Key<VT> key, AdderInterface<VT> adder) {
    VT sum = adder.zero();
    for (T elem : elements) {
      sum = adder.add(sum, elem.get(key));
    }
    return sum;
  }

  private <VT extends Comparable<VT>> VT max(Attributes.Key<VT> key) {
    assert (!elements.isEmpty());
    VT result = elements.get(0).get(key);
    for (T elem : elements) {
      if (elem.get(key).compareTo(result) > 0) {
        result = elem.get(key);
      }
    }
    return result;
  }

  private <VT extends Comparable<VT>> VT min(Attributes.Key<VT> key) {
    assert (!elements.isEmpty());
    VT result = elements.get(0).get(key);
    for (T elem : elements) {
      if (elem.get(key).compareTo(result) < 0) {
        result = elem.get(key);
      }
    }
    return result;
  }

  private List<T> elements;


}
