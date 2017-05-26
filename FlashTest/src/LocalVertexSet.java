import java.util.Iterator;

public class LocalVertexSet<T> implements FlashVertexSet {
  public static void main(String[] args) throws Exception {
    LocalVertexSet<Long> vertices = new LocalVertexSet<Long>();
    Vertex<Long> v1 = new Vertex<Long>(1L);
    v1.set("name", "Robin");
    v1.set("age", 10);
    v1.set("sex", true);

    Attribute skill1 = new Attribute();
    skill1.set("lang", "Java");
    skill1.set("year", 5);

    Attribute skill2 = new Attribute();
    skill2.set("lang", "C++");
    skill2.set("year", 2);

    Attribute skill3 = new Attribute();
    skill3.set("programming", "Python");
    skill3.set("year", 1);

    AttributeSet<Attribute> skillSet = new AttributeSet<>();
    skillSet.append(skill1).append(skill2).append(skill3);
    v1.set("skill", skillSet);

    Key<AttributeSet<Attribute>> setKey =
        (Key<AttributeSet<Attribute>>) Attribute.key("skill", skillSet.getClass());

    v1.get(setKey).append(skill1).append(skill2).append(skill3);

    Vertex<Long> v2 = new Vertex<Long>(2L);
    v2.set("name", "Mia");
    v2.set("age", 5);
    v2.set("sex", false);
    v2.set("skill", new AttributeSet<Attribute>());
    v2.get(setKey).append(skill1).append(skill2);

    Vertex<Long> v3 = new Vertex<Long>(3L);
    v3.set("name", "Tricia");
    v3.set("age", 13);
    v3.set("sex", false);
    v3.set("skill", new AttributeSet<Attribute>());
    v3.get(setKey).append(skill3);

    vertices.append(v1).append(v2).append(v3);

    AggFunction<Attribute, Integer> sum = new AggFunction<Attribute, Integer>(setKey,
        Attribute.key("year", Integer.class), AggregationType.SUM);
    System.out.println(vertices.local("whole_skill", sum));
  }

  public LocalVertexSet() {
    this.vertexSet = new AttributeSet<Vertex<T>>();
  }

  @Override
  public <VT extends Comparable<VT>> FlashVertexSet filter(Attribute.Predicate<VT> predicate) {
    // TODO Auto-generated method stub
    vertexSet = (AttributeSet<Vertex<T>>) this.vertexSet.filter(predicate);
    return this;
  }

  @Override
  public <ET> FlashVertexSet append(ET object) {
    if (!(object instanceof Vertex<?>)) {
      return this;
    }
    Vertex<T> newVertices = (Vertex<T>) object;
    vertexSet.append(newVertices);
    return this;
  }

  @Override
  public <VT> FlashVertexSet local(String newKey, LocalFunction<VT> function) throws Exception {
    // TODO Auto-generated method stub
    Iterator<Vertex<T>> iter = this.vertexSet.iterator();
    while (iter.hasNext()) {
      Vertex<T> v = iter.next();
      function.setV(v);
      v.set(newKey, function.apply());
    }
    return this;
  }

  @Override
  public String toString() {
    return this.vertexSet.toString();
  }

  private AttributeSet<Vertex<T>> vertexSet;



}
