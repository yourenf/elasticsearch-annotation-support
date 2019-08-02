package org.elasticsearch.extra.query.plugin.converter;

import org.junit.Assert;
import org.junit.Test;

public class BeanConverterFactoryTest {

  @Test
  public void findConverter() {
    BeanConverterFactory factory = new BeanConverterFactory();
    factory.addConverterFactory(A.class, new AConverterFactory());
    Converter<A1, ?> converter = factory.findConverter(A1.class);
    Converter<A2, ?> converter2 = factory.findConverter(A2.class);
    Converter<A3, ?> converter3 = factory.findConverter(A3.class);
    Assert.assertEquals(new A1().name(), converter.apply(new A1()));
    Assert.assertEquals(new A2().name(), converter2.apply(new A2()));
    Assert.assertEquals(new A3().name(), converter3.apply(new A3()));
  }

  public interface A {
    String name();
  }

  public static class A1 implements A {
    @Override
    public String name() {
      return "a1";
    }
  }

  public static class A2 implements A {
    @Override
    public String name() {
      return "a2";
    }
  }

  public static class A3 implements A {
    @Override
    public String name() {
      return "a3";
    }
  }

  public static class AConverterFactory implements ConverterFactory<A, String> {

    @Override
    public <T extends A> Converter<A, String> getConverter(Class<T> targetType) {
      return a -> a.name();
    }
  }
}