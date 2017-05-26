
public interface LocalFunction<T> {
  public T apply() throws Exception;

  public void setV(Vertex<?> v);
}


abstract class SetLocalFunction<I extends Set<?>, O> implements LocalFunction<O> {

  public SetLocalFunction(Key<I> setAttrKey, Key<O> setAttrOprKey) {
    this.setAttrKey = setAttrKey;
    this.setAttrOprKey = setAttrOprKey;
  }

  protected Key<I> setAttrKey;
  protected Key<O> setAttrOprKey;
}


class AggFunction<ST extends Attribute, O extends Comparable<O>>
    extends SetLocalFunction<AttributeSet<ST>, O> {

  public AggFunction(Key<AttributeSet<ST>> setAttrKey, Key<O> setAttrOprKey,
      AggregationType aggType) {
    super(setAttrKey, setAttrOprKey);
    this.aggType = aggType;
  }

  @Override
  public O apply() throws Exception {
    // TODO Auto-generated method stub
    O result = null;
    switch (this.aggType) {
      case SUM:
        result = vertex.get(setAttrKey).aggregation(setAttrOprKey, AggregationType.SUM);
        break;
      case MAX:
        result = vertex.get(setAttrKey).aggregation(setAttrOprKey, AggregationType.MAX);
        break;
      case MIN:
        result = vertex.get(setAttrKey).aggregation(setAttrOprKey, AggregationType.MIN);
        break;
      default:
        throw new Exception("Aggregation Type not supported");
    }
    return result;
  }

  @Override
  public void setV(Vertex<?> v) {
    // TODO Auto-generated method stub
    this.vertex = v;
  }

  private Vertex<?> vertex;
  private AggregationType aggType;
}
