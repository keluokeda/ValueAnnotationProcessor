package com.ke;


import com.ke.annotation.Price;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

//保存注解上的相关信息
public class PriceAnnotatedField {

    private Name filedName;
    private int value;

    PriceAnnotatedField(Element element) {

        //如果当前元素的类型不是 成员变量 类型
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(String.format("only fields can be annotated with @%s", Price.class.getSimpleName()));
        }
        VariableElement variableElement = (VariableElement) element;
        this.filedName = variableElement.getSimpleName();
        Price price = variableElement.getAnnotation(Price.class);
        this.value = price.value();

    }


    Name getFieldName() {
        return this.filedName;
    }

    int getFiledValue() {
        return this.value;
    }
}
