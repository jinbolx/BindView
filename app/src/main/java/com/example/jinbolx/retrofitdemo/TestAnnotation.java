package com.example.jinbolx.retrofitdemo;

import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {

    @TestAnnotation
    public class A {

    }

    public class B extends A {

    }
    @interface Persons{
        Person[] value();
    }
    @RequiresApi(api = VERSION_CODES.N)
    @Repeatable(Persons.class)
    @interface Person{
        String role() default "";
        int id() default 0;
        public int num() default -1;
    }
    @Person(role = "1")
    public class SuperMan{}
    @Person(id = 10,role = "ee",num = 1)
    public class Test{
        @Deprecated
        public void say(){}
        public void say(int a){}
    }

}
