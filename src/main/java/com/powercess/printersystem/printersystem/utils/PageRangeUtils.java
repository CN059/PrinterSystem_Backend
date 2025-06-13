package com.powercess.printersystem.printersystem.utils;

import java.util.HashSet;
import java.util.Set;

public class PageRangeUtils {
    public static int parseCustomPageRange(String customRange) {
        if (customRange == null || customRange.trim().isEmpty()) {
            return 0;
        }
        Set<Integer> pages = new HashSet<>();
        String[] ranges = customRange.split(",");
        for (String range : ranges) {
            if (range.contains("-")) {
                String[] bounds = range.split("-");
                int start = Integer.parseInt(bounds[0]);
                int end = Integer.parseInt(bounds[1]);
                for (int i = start; i <= end; i++) {
                    pages.add(i);
                }
            } else {
                pages.add(Integer.parseInt(range));
            }
        }
        return pages.size();
    }
}