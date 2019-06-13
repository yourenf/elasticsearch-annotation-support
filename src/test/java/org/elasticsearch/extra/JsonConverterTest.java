package org.elasticsearch.extra;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import java.util.LinkedHashMap;
import java.util.Map;


public class JsonConverterTest {
  public static final class DePinyinCounter implements Converter<Map<String, String>, String> {

    private static final String MAIN_NAME = "name";

    @Override
    public String convert(Map<String, String> value) {
      return value.get(MAIN_NAME);
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
      return typeFactory.constructMapType(LinkedHashMap.class, String.class, String.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
      return typeFactory.constructType(String.class);
    }
  }

  public static final class PinyinCounter implements Converter<String, Map<String, String>> {

    private static final String OTHER_NAME = "pinyin";
    private static final String MAIN_NAME = "name";

    @Override
    public Map<String, String> convert(String value) {
      Map<String, String> map = new LinkedHashMap<>(2);
      map.put(MAIN_NAME, value);
      map.put(OTHER_NAME, value);
      return map;
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
      return typeFactory.constructType(String.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
      return typeFactory.constructMapType(LinkedHashMap.class, String.class, String.class);
    }
  }
}
