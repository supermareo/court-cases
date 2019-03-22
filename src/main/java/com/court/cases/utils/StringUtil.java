package com.court.cases.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtil {

    public static String ncrToCn(String unicode) {
        List<String> list = new ArrayList<>();
        String[] split = unicode.split("&#");
        for (String s : split) {
            String[] split1 = s.split(";");
            list.addAll(Arrays.asList(split1));
        }
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            if (s.length() == 5) {
                sb.append((char) Integer.valueOf(s).intValue());
            } else {
                sb.append(s);
            }
        }
        return sb.toString();
    }

}
