package com.aa.mtg.utils;

import org.junit.Test;

import static com.aa.mtg.utils.Utils.replaceLast;
import static org.assertj.core.api.Assertions.assertThat;

public class UtilsTest {

    @Test
    public void replaceLastTest() {
        assertThat(replaceLast("cat dog cat", "cat", "other")).isEqualTo("cat dog other");
    }
}