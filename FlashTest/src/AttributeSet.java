import java.util.List;

public class AttributeSet<T extends Attribute> extends Set<T> {
  
  public static void main(String[] args) throws Exception {
    AttributeSet<Attribute> set = new AttributeSet<>();
    Attribute attr1 = new Attribute();
    attr1.set("name", "longbin");
    attr1.set("age", 10);
    attr1.set("sex", true);
    
    Attribute attr2 = new Attribute();
    attr2.set("name", "miao");
    attr2.set("age", 5);
    attr2.set("sex", false);
    
    Attribute attr3 = new Attribute();
    attr3.set("name", "abby");
    attr3.set("age", 5);
    attr3.set("sex", false);
    
    set.append(attr1).append(attr2).append(attr3);
    System.out.println(set);
    System.out.println(set.order(Attribute.key("age", Integer.class), RankUtil.Order.ASC));
    System.out.println(set.order(Attribute.key("age", Integer.class), RankUtil.Order.DESC));
  }
  
  /**
   * 
   */
  private static final long serialVersionUID = 1052501672012100184L;

  public <VT extends Comparable<VT>> Set<T> filter(Attribute.Predicate<VT> predicator) {
    Set<T> filtered = new AttributeSet<>();
    for (T elem : elements) {
      if (elem.on(predicator)) {
        filtered.append(elem);
      }
    }
    return filtered;
  }

  public <VT extends Comparable<VT>> Set<T> filter(List<Attribute.Predicate<VT>> predicators) {
    Set<T> filtered = new AttributeSet<>();
    for (T elem : elements) {
      boolean allGood = true;
      for (Attribute.Predicate<VT> pred : predicators) {
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
  
  public <VT extends Comparable<VT>> Set<T> order(Key<VT> key, RankUtil.Order order)
      throws Exception {
    Attribute.OrderedByKeys<VT> comparator = new Attribute.OrderedByKeys<>(order);
    comparator.addKeysForSort(key);
    elements.sort(comparator);
    return this;
  }
  
  @SuppressWarnings("unchecked")
  public <VT extends Comparable<VT>> VT aggregation(Key<VT> key, AggregationType agg)
      throws Exception {

    switch (agg) {
      case SUM:
        if (key.type() == Integer.class) {
          return (VT) this.sum((Key<Integer>) key, new IntegerAdder());
        } else if (key.type() == Long.class) {
          return (VT) this.sum((Key<Long>) key, new LongAdder());
        } else if (key.type() == Float.class) {
          return (VT) this.sum((Key<Float>) key, new FloatAdder());
        } else if (key.type() == Double.class) {
          return (VT) this.sum((Key<Double>) key, new DoubleAdder());
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

  public <VT extends Comparable<VT>> VT aggregation(Key<VT> key, AggregationType agg,
      List<Attribute.Predicate<VT>> predicators) throws Exception {
    return ((AttributeSet<T>)this.filter(predicators)).aggregation(key, agg);
  }
  
  private <VT extends Number> VT sum(Key<VT> key, AdderInterface<VT> adder) {
    VT sum = adder.zero();
    for (T elem : elements) {
      sum = adder.add(sum, elem.get(key));
    }
    return sum;
  }

  private <VT extends Comparable<VT>> VT max(Key<VT> key) {
    assert (!elements.isEmpty());
    VT result = elements.get(0).get(key);
    for (T elem : elements) {
      if (elem.get(key).compareTo(result) > 0) {
        result = elem.get(key);
      }
    }
    return result;
  }

  private <VT extends Comparable<VT>> VT min(Key<VT> key) {
    assert (!elements.isEmpty());
    VT result = elements.get(0).get(key);
    for (T elem : elements) {
      if (elem.get(key).compareTo(result) < 0) {
        result = elem.get(key);
      }
    }
    return result;
  }
}