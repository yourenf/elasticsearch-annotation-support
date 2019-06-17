package org.elasticsearch.extra.query.plugin.converter;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ClassHierarchyTest {

  @Test
  public void getClassHierarchy() {
    ClassHierarchy hierarchy = new ClassHierarchy();

    Assert.assertEquals(2, hierarchy.getClassHierarchy(A.class).size());
    Assert.assertEquals(3, hierarchy.getClassHierarchy(A1.class).size());
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
}