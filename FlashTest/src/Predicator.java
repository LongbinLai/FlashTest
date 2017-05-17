
public class Predicator<T extends Comparable<T>> {
  public Predicator(Attributes.Key<T> key, RankUtil.CompareOprator opr, T val) {
    this.key = key;
    this.opr = opr;
    this.val = val;
  }

  public Attributes.Key<T> key() {
    return this.key;
  }

  public RankUtil.CompareOprator operator() {
    return this.opr;
  }

  public T value() {
    return this.val;
  }

  private final Attributes.Key<T> key;
  private RankUtil.CompareOprator opr;
  private T val;
}