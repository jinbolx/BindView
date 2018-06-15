package com.example.jinbolx.retrofitdemo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface FrescoLoadAnnotation {
    String url();

}
