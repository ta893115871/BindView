package com.example.model;

import com.example.BindLayout;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 * ================================================
 * 作    者：顾修忠-guxiuzhong@youku.com/gfj19900401@163.com
 * 版    本：
 * 创建日期：16/9/7-上午11:27
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class BindLayoutClass {
    private int mLayoutId;

    public BindLayoutClass(Element element) throws IllegalArgumentException {
        /*与其他注解解析不同，BindLayout 标注的类型就是 TYPE，所以这里直接强转为
      TypeElement，其实就是对应于 Activity 的类型*/
        if (element.getKind() != ElementKind.CLASS) {
            throw new IllegalArgumentException(String.format("Only fields can be annotated with @%s", BindLayout.class.getSimpleName()));
        }
        TypeElement typeElement = (TypeElement) element;
        mLayoutId = typeElement.getAnnotation(BindLayout.class).value();
        if (mLayoutId == 0) {
            throw new IllegalArgumentException(String.format(
                    "@%s for a Activity must specify one layout ID. Found: %s. (%s.%s)",
                    BindLayout.class.getSimpleName(), mLayoutId, typeElement.getQualifiedName(),
                    element.getSimpleName()
            ));
        }
    }

    public int getLayoutId() {
        return mLayoutId;
    }
}
