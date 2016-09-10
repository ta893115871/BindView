package com.example.model;

import com.example.OnClick;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;

/**
 * ================================================
 * 作    者：顾修忠-guxiuzhong@youku.com/gfj19900401@163.com
 * 版    本：
 * 创建日期：16/9/5-下午5:11
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class OnClickMethod {
    private Name mMethodName;
    public int[] ids;


    public OnClickMethod(Element element) throws IllegalArgumentException {
        if (element.getKind() != ElementKind.METHOD) {
            throw new IllegalArgumentException(String.format("Only methods can be annotated with @%s", OnClick.class.getSimpleName()));
        }
        ExecutableElement methodElement = (ExecutableElement) element;
        this.mMethodName = methodElement.getSimpleName();
        this.ids = methodElement.getAnnotation(OnClick.class).value();
        if (ids == null) {
            throw new IllegalArgumentException(String.format("Must set valid ids for @%s", OnClick.class.getSimpleName()));
        } else {
            for (int id : ids) {
                if (id < 0) {
                    throw new IllegalArgumentException(String.format("Must set valid id for @%s", OnClick.class.getSimpleName()));
                }
            }
        }

        // method params count must equals 0
        List<? extends VariableElement> parameters = methodElement.getParameters();
        if (parameters.size() > 0) {
            throw new IllegalArgumentException(
                    String.format("The method annotated with @%s must have no parameters", OnClick.class.getSimpleName()));
        }
    }

    public Name getMethodName() {
        return mMethodName;
    }
}
