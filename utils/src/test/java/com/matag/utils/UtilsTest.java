package com.matag.utils;

import org.junit.Test;

import static com.matag.utils.Utils.replaceLast;
import static org.assertj.core.api.Assertions.assertThat;

public class UtilsTest {

  @Test
  public void replaceLastTest() {
    assertThat(replaceLast("cat dog cat", "cat", "other")).isEqualTo("cat dog other");
  }
}