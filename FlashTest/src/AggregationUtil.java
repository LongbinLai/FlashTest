
enum AggregationType {
  SUM, MAX, MIN
};


interface AdderInterface<T extends Number> {
  T zero();

  T add(T lhs, T rhs);
}


class IntegerAdder implements AdderInterface<Integer> {

  @Override
  public Integer zero() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Integer add(Integer lhs, Integer rhs) {
    // TODO Auto-generated method stub
    return lhs + rhs;
  }
}


class LongAdder implements AdderInterface<Long> {

  @Override
  public Long zero() {
    // TODO Auto-generated method stub
    return 0L;
  }

  @Override
  public Long add(Long lhs, Long rhs) {
    // TODO Auto-generated method stub
    return lhs + rhs;
  }
}


class DoubleAdder implements AdderInterface<Double> {

  @Override
  public Double zero() {
    // TODO Auto-generated method stub
    return 0.0;
  }

  @Override
  public Double add(Double lhs, Double rhs) {
    // TODO Auto-generated method stub
    return lhs + rhs;
  }
}


class FloatAdder implements AdderInterface<Float> {

  @Override
  public Float zero() {
    // TODO Auto-generated method stub
    return 0f;
  }

  @Override
  public Float add(Float lhs, Float rhs) {
    // TODO Auto-generated method stub
    return lhs + rhs;
  }
}
