package com.powercess.printersystem.printersystem.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PrintPriceCalculator {
    // 每张价格
    private static final BigDecimal PRICE_SINGLE_SIDE = new BigDecimal("0.20");
    private static final BigDecimal PRICE_DOUBLE_SIDE = new BigDecimal("0.25");
    // 等级折扣
    private static final BigDecimal[] DISCOUNTS = {
            BigDecimal.ONE,                 // lv1
            new BigDecimal("0.99"),         // lv2
            new BigDecimal("0.98"),         // lv3
            new BigDecimal("0.97"),         // lv4
            new BigDecimal("0.96"),         // lv5
            new BigDecimal("0.95")          // lv6
    };
    /**
     * 计算实际打印张数（单面：一页一张；双面：两页一张）
     */
    public static int calculatePrintSheets(int totalPages, boolean duplex) {
        if (duplex) {
            return (int) Math.ceil((double) totalPages / 2);
        } else {
            return totalPages;
        }
    }
    /**
     * 根据等级获取折扣率
     */
    public static BigDecimal getDiscountByLevel(int level) {
        if (level < 1 || level > DISCOUNTS.length) {
            return BigDecimal.ONE;
        }
        return DISCOUNTS[level - 1];
    }
    /**
     * 计算最终价格
     */
    public static BigDecimal calculatePrice(int sheets, boolean duplex, int userLevel) {
        BigDecimal pricePerSheet = duplex ? PRICE_DOUBLE_SIDE : PRICE_SINGLE_SIDE;
        BigDecimal totalPrice = pricePerSheet.multiply(BigDecimal.valueOf(sheets));
        BigDecimal discount = getDiscountByLevel(userLevel);
        return totalPrice.multiply(discount).setScale(2, RoundingMode.HALF_UP);
    }
}