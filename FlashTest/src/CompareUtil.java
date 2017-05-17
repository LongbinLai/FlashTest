
public class CompareUtil {
  
  public enum BiOprator {
    GT, GE, LT, LE, EQ, NE
  };

  public final static <T extends Comparable<T>> boolean compare(T x, BiOprator opr, T y) {
    switch (opr) {
      case GT:
        return x.compareTo(y) > 0;
      case GE:
        return x.compareTo(y) >= 0;
      case LT:
        return x.compareTo(y) < 0;
      case LE:
        return x.compareTo(y) <= 0;
      case EQ:
        return x.equals(y);
      case NE:
        return !x.equals(y);
    }
    return false;
  }
}