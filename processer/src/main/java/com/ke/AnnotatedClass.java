package com.ke;


import com.ke.annotation.Price;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class AnnotatedClass {

    private static ClassName INTERFACE_CLASS_NAME = ClassName.get("com.keluokeda.api", "PriceInject");

    //一个类中所有含有注解的 filed
    private List<PriceAnnotatedField> mFields = new ArrayList<>(1);
    private TypeElement mTypeElement;
    private Elements mElements;


    AnnotatedClass(TypeElement typeElement, Elements elements) {
        mTypeElement = typeElement;
        mElements = elements;
    }

    JavaFile createJavaFile() {


        MethodSpec.Builder builder =
                //生成 方法名称 和 参数名称
                // override
                // public void inject(T tName)
                MethodSpec.methodBuilder("inject")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(getTypeName(), "target");

        for (PriceAnnotatedField filed : getFields()) {
            builder.addStatement("target.$N = $L", filed.getFieldName(), filed.getFiledValue());
        }


        TypeSpec typeSpec = TypeSpec.
                classBuilder(getSimpleClassName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(builder.build())
                .addSuperinterface(ParameterizedTypeName.get(INTERFACE_CLASS_NAME, getTypeName()))
                .build();


        return JavaFile.builder(getPackageName(), typeSpec).build();

    }

    void addField(PriceAnnotatedField field) {
        mFields.add(field);
    }


    //获取 一个类元素的包名
    private String getPackageName() {
        return mElements.getPackageOf(mTypeElement).getQualifiedName().toString();
    }


    private TypeName getTypeName() {
        return TypeName.get(mTypeElement.asType());
    }

    private List<PriceAnnotatedField> getFields() {
        return mFields;
    }

    private String getSimpleClassName() {
        // TODO: 2017/5/28  
        return mTypeElement.getSimpleName() + "_PriceInject" ;
    }


}
