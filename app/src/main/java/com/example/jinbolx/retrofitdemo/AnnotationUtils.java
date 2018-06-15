package com.example.jinbolx.retrofitdemo;

import com.example.jinbolx.retrofitdemo.TestAnnotation.Person;
import com.example.jinbolx.retrofitdemo.TestAnnotation.Test;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AnnotationUtils {

    public static void init() {
        Test test = new Test();
        test.say();
        test.say(1);
        Class c = Test.class;
        boolean hasAnnotation = c.isAnnotationPresent(Person.class);
        if (hasAnnotation) {
            System.out.println("has annotation");
            Person person = (Person) c.getAnnotation(Person.class);
            System.out.println(
                    "id: " + person.id() + " num: " + person.num() + " role: " + person.role());
        } else {
            System.out.println("no annotation");
        }
        annotationTest();
    }

    @interface Check {

    }

    static class NoBug {

        @Check
        public void plus() {
            System.out.println("1+1= " + (1 + 1));
        }
        @Check
        public void subtraction() {
            System.out.println("1-2= " + (1 - 2));
        }
        @Check
        public void multiplication(){
            System.out.println("2*2= "+2*2);
        }
        @Check
        public void division(){
            System.out.println("2/3= "+2/3);
        }
        public void noBugs(){
            System.out.println("no bugs");
        }

    }
    public static void annotationTest(){
       NoBug noBug=new NoBug();
       Class c=noBug.getClass();
        Method[] methods=c.getDeclaredMethods();
        StringBuilder log=new StringBuilder();
        int errorNum=0;
        for (Method m :
                methods) {
            if (m.isAnnotationPresent(Check.class)) {
                m.setAccessible(true);
                try {
                    m.invoke(noBug);
                } catch (Exception e) {
                    e.printStackTrace();
                    errorNum++;
                    log.append(m.getName()).append(" has error");
                    log.append("  caused by");
                    log.append(e.getClass().getSimpleName());
                    log.append("\n\r");
                    log.append(e.getCause().getMessage());
                    log.append("\n\r");

                }
            }
        }
        log.append(noBug.getClass().getSimpleName());
        log.append("  has ");
        log.append(errorNum);
        log.append("  error");
        System.out.println(log.toString());
    }
    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.FIELD)
    @interface FindView{
        int max() default 100;
        int min() default 0;
    }
}

