package org.elasticsearch.extra.query.plugin.converter;

import java.lang.reflect.Array;
import java.util.*;

public class ClassHierarchy {
  private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new IdentityHashMap<>(8);

  static {
    primitiveTypeToWrapperMap.put(boolean.class, Boolean.class);
    primitiveTypeToWrapperMap.put(byte.class, Byte.class);
    primitiveTypeToWrapperMap.put(char.class, Character.class);
    primitiveTypeToWrapperMap.put(double.class, Double.class);
    primitiveTypeToWrapperMap.put(float.class, Float.class);
    primitiveTypeToWrapperMap.put(int.class, Integer.class);
    primitiveTypeToWrapperMap.put(long.class, Long.class);
    primitiveTypeToWrapperMap.put(short.class, Short.class);
  }


  /**
   * Returns an ordered class hierarchy for the given type.
   *
   * @param type the type
   * @return an ordered list of all classes that the given type extends or implements
   */
  public List<Class<?>> getClassHierarchy(Class<?> type) {
    List<Class<?>> hierarchy = new ArrayList<>(20);
    Set<Class<?>> visited = new HashSet<>(20);
    addToClassHierarchy(0, resolvePrimitiveIfNecessary(type), false, hierarchy, visited);
    boolean array = type.isArray();

    int i = 0;
    while (i < hierarchy.size()) {
      Class<?> candidate = hierarchy.get(i);
      candidate = (array ? candidate.getComponentType() : resolvePrimitiveIfNecessary(candidate));
      Class<?> superclass = candidate.getSuperclass();
      if (superclass != null && superclass != Object.class && superclass != Enum.class) {
        addToClassHierarchy(i + 1, candidate.getSuperclass(), array, hierarchy, visited);
      }
      addInterfacesToClassHierarchy(candidate, array, hierarchy, visited);
      i++;
    }

    if (Enum.class.isAssignableFrom(type)) {
      addToClassHierarchy(hierarchy.size(), Enum.class, array, hierarchy, visited);
      addToClassHierarchy(hierarchy.size(), Enum.class, false, hierarchy, visited);
      addInterfacesToClassHierarchy(Enum.class, array, hierarchy, visited);
    }

    addToClassHierarchy(hierarchy.size(), Object.class, array, hierarchy, visited);
    addToClassHierarchy(hierarchy.size(), Object.class, false, hierarchy, visited);
    return hierarchy;
  }

  private void addInterfacesToClassHierarchy(Class<?> type, boolean asArray,
                                             List<Class<?>> hierarchy, Set<Class<?>> visited) {

    for (Class<?> implementedInterface : type.getInterfaces()) {
      addToClassHierarchy(hierarchy.size(), implementedInterface, asArray, hierarchy, visited);
    }
  }

  private void addToClassHierarchy(int index, Class<?> type, boolean asArray,
                                   List<Class<?>> hierarchy, Set<Class<?>> visited) {

    if (asArray) {
      type = Array.newInstance(type, 0).getClass();
    }
    if (visited.add(type)) {
      hierarchy.add(index, type);
    }
  }

  private Class<?> resolvePrimitiveIfNecessary(Class<?> clazz) {
    Objects.requireNonNull(clazz, "Class must not be null");
    return (clazz.isPrimitive() && clazz != void.class ? primitiveTypeToWrapperMap.get(clazz) : clazz);
  }

}
