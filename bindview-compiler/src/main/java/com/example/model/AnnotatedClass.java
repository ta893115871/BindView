package com.example.model;

import com.example.TypeUtil;
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

/**
 * ================================================
 * 作    者：顾修忠-guxiuzhong@youku.com/gfj19900401@163.com
 * 版    本：
 * 创建日期：16/9/5-下午5:02
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class AnnotatedClass {
    public TypeElement mClassElement; //类名
    public List<BindViewField> mFields;//成员变量
    public List<OnClickMethod> mMethods;//方法
    private BindLayoutClass bindLayoutClass;
    public Elements mElementUtils;


    public AnnotatedClass(TypeElement classElement, Elements elementUtils) {
        this.mClassElement = classElement;
        this.mFields = new ArrayList<>();
        this.mMethods = new ArrayList<>();
        this.mElementUtils = elementUtils;
    }

    public String getFullClassName() {
        return mClassElement.getQualifiedName().toString();
    }

    public void addField(BindViewField field) {
        mFields.add(field);
    }

    public void addMethod(OnClickMethod method) {
        mMethods.add(method);
    }

    public void setContentLayoutId(BindLayoutClass bindLayoutClass) {
        this.bindLayoutClass = bindLayoutClass;
    }

    public JavaFile generateFinder() {
        // method inject(final T host, Object source, Provider provider)
        MethodSpec.Builder injectMethodBuilder = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mClassElement.asType()), "host", Modifier.FINAL)
                .addParameter(TypeName.OBJECT, "source")
                .addParameter(TypeUtil.FINDER, "finder");


        // setContentView
        if (bindLayoutClass!=null){
            if(bindLayoutClass.getLayoutId() != 0){
                injectMethodBuilder.addStatement("host.setContentView($L)", bindLayoutClass.getLayoutId());
            }
        }

        //field
        for (BindViewField field : mFields
                ) {
            injectMethodBuilder.addStatement("host.$N= ($T)(finder.findView(source,$L))", field.getFieldName()
                    , ClassName.get(field.getFieldType()), field.getResId());

        }
        if (mMethods.size() > 0) {
            injectMethodBuilder.addStatement("$T listener", TypeUtil.ANDROID_ON_CLICK_LISTENER);
        }
        for (OnClickMethod method : mMethods) {
            // declare OnClickListener anonymous class
            TypeSpec listener = TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(TypeUtil.ANDROID_ON_CLICK_LISTENER)
                    .addMethod(MethodSpec.methodBuilder("onClick")
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(TypeName.VOID)
                            .addParameter(TypeUtil.ANDROID_VIEW, "view")
                            .addStatement("host.$N()", method.getMethodName())
                            .build()).build();
            injectMethodBuilder.addStatement("listener = $L ", listener);

            for (int id : method.ids) {
                // set listeners
                injectMethodBuilder.addStatement("finder.findView(source, $L).setOnClickListener(listener)", id);
            }
        }

        String packageName = getPackageName(mClassElement);
        String className = getClassName(mClassElement, packageName);
        ClassName bindingClassName = ClassName.get(packageName, className);

        System.out.println("------mClassElement-getSimpleName------>>>>" + mClassElement.getSimpleName().toString());
        System.out.println("-------bindingClassName-simpleName----->>>>" + bindingClassName.simpleName());

        // generate whole class
        TypeSpec finderClass = TypeSpec.classBuilder(bindingClassName.simpleName() + "$$Injector")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(TypeUtil.INJECTOR, TypeName.get(mClassElement.asType())))
                .addMethod(injectMethodBuilder.build())
                .build();

        return JavaFile.builder(packageName, finderClass).build();
    }

    private String getPackageName(TypeElement type) {
        return mElementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }

}
