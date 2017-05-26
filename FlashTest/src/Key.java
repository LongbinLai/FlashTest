
public class Key<T> {
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