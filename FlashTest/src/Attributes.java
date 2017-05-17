import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class Attributes implements Serializable, Comparable<Attributes> {

  public static void main(String[] args) {
    Attributes attribute = new Attributes();
    attribute.set("age", 1);
    attribute.set("name", "Longbin");
    attribute.set("sex", true);
    System.out.println(attribute.get(Attributes.key("age", Integer.class)));
    System.out.println(attribute.get(Attributes.key("name", String.class)));
    System.out.println(attribute.get(Attributes.key("sex", Boolean.class)));
    System.out.println(attribute
        .on(new Predicator<>(Attributes.key("age", Integer.class), RankUtil.CompareOprator.GE, 1)));
    System.out.println(attribute
        .on(new Predicator<>(Attributes.key("age", Integer.class), RankUtil.CompareOprator.GE, 20)));
    
    Attributes attribute2 = new Attributes();
    attribute2.set("age", 5);
    attribute2.set("name", "Longbin");
    attribute2.set("sex", true);
    try {
      attribute.addKeyForSort(Attributes.key("age", Integer.class));
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.out.println(attribute.compareTo(attribute2));
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

  public Attributes() {
    attributeMap = new HashMap<>();
    keysForSort = new ArrayList<>();
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
    attributeMap.put(Attributes.key(key, value.getClass()), value);
  }
  
  public <T> void set(Key<T> key, T value) {
    attributeMap.put(key, value);
  }

  public <T> boolean hasAttribute(Key<T> key) {
    return attributeMap.containsKey(key);
  }
  
  public <T> T remove(Key<T> key) {
    keysForSort.remove(key);
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
  public <T extends Comparable<T>> boolean on(Predicator<T> predicator) {
    T attrVal = null;
    try {
      attrVal = this.get(predicator.key());
    } catch (NoSuchElementException e) {
      return true;
    }
    return RankUtil.compare(attrVal, predicator.operator(), predicator.value());
  }
  
  public <T extends Comparable<T>> void addKeyForSort(Key<T> key) throws Exception {
    if(!keysForSort.contains(key)) { 
      T value = get(key);
      if (!(value instanceof Comparable)) {
        throw new Exception("The value of the key is not comparable.");
      }
      keysForSort.add(key);
    }
  }
  
  @Override
  public int compareTo(Attributes other) {
    if (keysForSort.isEmpty()) {
      return 0;
    }
    int comp = 0;
    for (Key<?> key : keysForSort) {
      if (!hasAttribute(key) || !other.hasAttribute(key)) {
        return 0;
      }
      Comparable value1 = (Comparable) get(key);
      Comparable value2 = (Comparable) other.get(key);
      comp = value1.compareTo(value2);
      if (comp != 0) {
        break;
      }
    }
    return comp;
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
  // Store the keys for sort
  private List<Key<?>> keysForSort;
  private Map<Key<?>, Object> attributeMap;

}
