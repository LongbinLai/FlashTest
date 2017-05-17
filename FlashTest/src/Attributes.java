import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class Attributes implements Serializable {

  public static void main(String[] args) {
    Attributes attribute = new Attributes();
    attribute.set(Attributes.key("age", Integer.class), 1);
    attribute.set(Attributes.key("name", String.class), "Longbin");
    attribute.set(Attributes.key("sex", Boolean.class), true);
    System.out.println(attribute.get(Attributes.key("age", Integer.class)));
    System.out.println(attribute.get(Attributes.key("name", String.class)));
    System.out.println(attribute.get(Attributes.key("sex", Boolean.class)));
    System.out.println(
        attribute.on(new Predicator<>(Attributes.key("age", Integer.class), CompareUtil.BiOprator.GE, 1)));
    System.out.println(
        attribute.on(new Predicator<>(Attributes.key("age", Integer.class), CompareUtil.BiOprator.GE, 20)));
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

  private static final long serialVersionUID = 1L;

  public Attributes() {
    attrMap = new HashMap<>();
  }

  public static <T> Key<T> key(String id, Class<T> type) {
    return new Key<>(id, type);
  }

  public <T> T get(Key<T> key) throws NoSuchElementException {
    if (!attrMap.containsKey(key)) {
      throw new NoSuchElementException("key: " + key);
    }
    return key.type.cast(attrMap.get(key));
  }

  public <T> boolean set(Key<T> key, T value) {
    boolean isExist = attrMap.containsKey(key);
    attrMap.put(key, value);
    return isExist;
  }

  public <T> boolean hasAttribute(Key<T> key) {
    return attrMap.containsKey(key);
  }

  /**
   * Given a key of the attribute, an binary operator, and the value to compare, return the result
   * of attrMap[key] opr val.
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
    return CompareUtil.compare(attrVal, predicator.operator(), predicator.value());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(StringUtil.ARRAY_START);
    int count = 0, size = attrMap.size();
    for (Map.Entry<Key<?>, Object> entry : attrMap.entrySet()) {
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

  private Map<Key<?>, Object> attrMap;
}
