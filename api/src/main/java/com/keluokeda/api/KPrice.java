package com.keluokeda.api;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class KPrice {
    private static final Map<String, PriceInject> STRING_PRICE_INJECT_MAP = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
    public static void injectPrice(Object object) {
        //类全名是唯一索引
        String className = object.getClass().getName();

        //先从 map中看下以前有没有存储过
        PriceInject priceInject = STRING_PRICE_INJECT_MAP.get(className);
        try {
            if (priceInject == null) {
                //如果没有就创建新的
                Class<?> clazz = Class.forName(className + "_" + PriceInject.class.getSimpleName());
                priceInject = (PriceInject) clazz.newInstance();
                STRING_PRICE_INJECT_MAP.put(className, priceInject);

            }
            priceInject.inject(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
