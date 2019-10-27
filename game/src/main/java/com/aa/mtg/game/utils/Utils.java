package com.aa.mtg.game.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {
    public static Map<Integer, List<Integer>> toMapListInteger(Map<Integer, List<Object>> mapListObject) {
        return mapListObject.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> toListInteger(entry.getValue())));
    }

    private static List<Integer> toListInteger(List<Object> listObject) {
        return listObject.stream().map((item) -> (int) item).collect(Collectors.toList());
    }

    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)"+regex+"(?!.*?"+regex+")", replacement);
    }
}
