import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class Attribute implements Serializable {

  public static class OrderedByKeys<T extends Comparable<T>>
      implements Comparator<Attribute> {

    public OrderedByKeys(RankUtil.Order order) {
      this.keysForSort = new ArrayList<Key<T>>();
      this.order = order;
    }

    public OrderedByKeys(List<Key<T>> keys, RankUtil.Order order) {
      this.keysForSort = new ArrayList<Key<T>>();
      this.keysForSort.addAll(keys);
      this.order = order;
    }

    public void addKeysForSort(Key<T> key) {
      // Note: An O(n) operation, keys must be relatively short
      if (!keysForSort.contains(key)) {
        this.keysForSort.add(key);
      }
    }

    @Override
    public int compare(Attribute o1, Attribute o2) {
      int comp = 0;
      for (Key<T> key : keysForSort) {
        if (!o1.hasAttribute(key) || !o2.hasAttribute(key)) {
          return 0;
        }
        comp = o1.get(key).compareTo(o2.get(key));
        if (comp != 0) {
          break;
        }
      }
      return order == RankUtil.Order.ASC ? comp : -comp;
    }

    private List<Key<T>> keysForSort;
    private RankUtil.Order order;

  }

  public static class Key<T> {
    public Key(String id, Class<T> type) {
      this.identifier = id;
      this.type = type;
    }

    public Class<T> type() {
      return this.type;
    }

    public String identifier() {
      return this.identifier;
    }

    @Override
    public int hashCode() {
      return this.identifier.hashCode();
    }

    @Override
    public boolean equals(Object o) {
      if (o == this)
        return true;
      if (!(o instanceof Key<?>)) {
        return false;
      }
      Key<T> that = (Key<T>) o;
      return this.identifier.equals(that.identifier()) && this.type.equals(that.type());
    }

    @Override
    public String toString() {
      return this.identifier;
    }

    private final String identifier;
    private final Class<T> type;
  }

  static class Predicate<T extends Comparable<T>> {
    public Predicate(Attribute.Key<T> key, RankUtil.CompareOprator opr, T val) {
      this.key = key;
      this.opr = opr;
      this.val = val;
    }

    public Attribute.Key<T> key() {
      return this.key;
    }

    public RankUtil.CompareOprator operator() {
      return this.opr;
    }

    public T value() {
      return this.val;
    }

    private final Attribute.Key<T> key;
    private RankUtil.CompareOprator opr;
    private T val;
  }

  public Attribute() {
    attributeMap = new HashMap<>();
  }

  public static <T> Key<T> key(String id, Class<T> type) {
    return new Key<>(id, type);
  }

  public <T> T get(Key<T> key) throws NoSuchElementException {
    if (!attributeMap.containsKey(key)) {
      throw new NoSuchElementException("key: " + key);
    }
    return key.type.cast(attributeMap.get(key));
  }

  public <T> void set(String key, T value) {
    attributeMap.put(Attribute.key(key, value.getClass()), value);
  }

  public <T> void set(Key<T> key, T value) {
    attributeMap.put(key, value);
  }

  public <T> boolean hasAttribute(Key<T> key) {
    return attributeMap.containsKey(key);
  }

  public <T> T remove(Key<T> key) {
    return key.type().cast(attributeMap.remove(key));
  }

  /**
   * Given a key of the attribute, an binary operator, and the value to compare, return the result
   * of attributeMap[key] opr val.
   * 
   * @param key
   * @param opr
   * @param val
   * @return
   */
  public <T extends Comparable<T>> boolean on(Predicate<T> predicator) {
    T attrVal = null;
    try {
      attrVal = this.get(predicator.key());
    } catch (NoSuchElementException e) {
      return true;
    }
    return RankUtil.compare(attrVal, predicator.operator(), predicator.value());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(StringUtil.ARRAY_START);
    int count = 0, size = attributeMap.size();
    for (Map.Entry<Key<?>, Object> entry : attributeMap.entrySet()) {
      sb.append(StringUtil.PAIR_START);
      sb.append(entry.getKey());
      sb.append(StringUtil.PAIR_SPLITTER);
      sb.append(entry.getValue());
      sb.append(StringUtil.PAIR_END);
      if (++count != size) {
        sb.append(StringUtil.ARRAY_SPLITTER);
      }
    }
    sb.append(StringUtil.ARRAY_END);
    return sb.toString();
  }

  private static final long serialVersionUID = 1L;
  private Map<Key<?>, Object> attributeMap;

}
