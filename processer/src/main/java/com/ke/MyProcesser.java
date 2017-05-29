package com.ke;


import com.google.auto.service.AutoService;
import com.ke.annotation.Price;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class MyProcesser extends AbstractProcessor {

    private Filer mFiler;
    private Elements mElements;
    private Messager mMessager;
    private Map<String, AnnotatedClass> mStringAnnotatedClassMap = new TreeMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mElements = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        Set<? extends Element> sets = roundEnvironment.getElementsAnnotatedWith(Price.class);

        if (sets.isEmpty()) {
            return true;
        }


        for (Element element : sets) {

            AnnotatedClass annotatedClass = getAnnotatedClass(element);
            PriceAnnotatedField field = new PriceAnnotatedField(element);
            annotatedClass.addField(field);


        }


        for (AnnotatedClass annotatedClass : mStringAnnotatedClassMap.values()) {
            /**
             * 生成 java 文件
             * 要生成的java文件要包含 一下信息
             *  void inject(T t){
             *  t.filedName = 123;
             *  }
             */

            try {
                annotatedClass.createJavaFile().writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
                error("create java file error %s", e.getMessage());
            }
        }


        return true;
    }

    //给开发者提供错误信息
    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    //根据 field 生成 annotatedClass
    private AnnotatedClass getAnnotatedClass(Element element) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        String fullName = typeElement.getQualifiedName().toString();
        AnnotatedClass annotatedClass = mStringAnnotatedClassMap.get(fullName);
        if (annotatedClass == null) {
            annotatedClass = new AnnotatedClass(typeElement, mElements);
            mStringAnnotatedClassMap.put(fullName, annotatedClass);
        }
        return annotatedClass;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> strings = new LinkedHashSet<>();
        strings.add(Price.class.getCanonicalName());
        return strings;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
