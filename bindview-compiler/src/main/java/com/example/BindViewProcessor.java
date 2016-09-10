package com.example;


import com.example.model.AnnotatedClass;
import com.example.model.BindLayoutClass;
import com.example.model.BindViewField;
import com.example.model.OnClickMethod;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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

/**
 * ================================================
 * 作    者：顾修忠-guxiuzhong@youku.com/gfj19900401@163.com
 * 版    本：
 * 创建日期：16/9/5-下午4:50
 * 描    述：http://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650236969&idx=1&sn=8bd0cf113c0aab1c82bd4bbc5497c0fb&scene=1&srcid=0905XA0Dy3pRN5kMTM2f8Z5D#rd
 * 修订历史：
 * ================================================
 */
@AutoService(Processor.class)
public class BindViewProcessor extends AbstractProcessor {
    /**
     * 使用 Google 的 auto-service 库可以自动生成 META-INF/services/javax.annotation.processing.Processor 文件
     */

    private Filer mFiler; //文件相关的辅助类
    private Elements mElementUtils; //元素相关的辅助类
    private Messager mMessager; //日志相关的辅助类
    private Map<String, AnnotatedClass> mAnnotatedClassMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
    }

    /**
     * @return 指定哪些注解应该被注解处理器注册
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(BindView.class.getCanonicalName());
        types.add(OnClick.class.getCanonicalName());
        types.add(BindLayout.class.getCanonicalName());
        return types;
    }

    /**
     * @return 指定使用的 Java 版本。通常返回 SourceVersion.latestSupported()。
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mAnnotatedClassMap.clear();
        try {
            processBindView(roundEnv);
            processOnClick(roundEnv);
            processBindLayout(roundEnv);
        } catch (IllegalArgumentException e) {
            error(e.getMessage());
            return true; // stop process
        }
        try {
            for (AnnotatedClass annotatedClass : mAnnotatedClassMap.values()) {
                info("Generating file for %s", annotatedClass.getFullClassName());
                annotatedClass.generateFinder().writeTo(mFiler);
            }
        } catch (IOException e) {
            e.printStackTrace();
            error("Generate file failed, reason: %s", e.getMessage());
        }
        return true; // stop process
    }


    private void processOnClick(RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(OnClick.class)) {
            AnnotatedClass annotatedClass = getAnnotatedClass(element);
            OnClickMethod method = new OnClickMethod(element);
            annotatedClass.addMethod(method);
        }
    }

    private void processBindView(RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(BindView.class)) {
            AnnotatedClass annotatedClass = getAnnotatedClass(element);
            BindViewField field = new BindViewField(element);
            annotatedClass.addField(field);
        }
    }

    private void processBindLayout(RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(BindLayout.class)) {
            AnnotatedClass annotatedClass = getAnnotatedClassForBindLayout(element);
            BindLayoutClass bindLayoutClass = new BindLayoutClass(element);
            annotatedClass.setContentLayoutId(bindLayoutClass);
        }
    }

    private AnnotatedClass getAnnotatedClassForBindLayout(Element element) {
        //不需要获取父类
        TypeElement enclosingElement = (TypeElement) element;
        String fullClassName = enclosingElement.getQualifiedName().toString();
        AnnotatedClass annotatedClass = mAnnotatedClassMap.get(fullClassName);
        if (annotatedClass == null) {
            annotatedClass = new AnnotatedClass(enclosingElement, mElementUtils);
            mAnnotatedClassMap.put(fullClassName, annotatedClass);
        }
        return annotatedClass;
    }

    private AnnotatedClass getAnnotatedClass(Element element) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        String fullClassName = enclosingElement.getQualifiedName().toString();
        AnnotatedClass annotatedClass = mAnnotatedClassMap.get(fullClassName);
        if (annotatedClass == null) {
            annotatedClass = new AnnotatedClass(enclosingElement, mElementUtils);
            mAnnotatedClassMap.put(fullClassName, annotatedClass);
        }
        return annotatedClass;
    }

    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    private void info(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }

}
