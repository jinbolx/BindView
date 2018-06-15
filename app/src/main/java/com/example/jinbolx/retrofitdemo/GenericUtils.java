package com.example.jinbolx.retrofitdemo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class GenericUtils {

    public static void init() {
        List<String> list=new ArrayList<>();
        list.add("hello");
        System.out.println("list.size: "+list.size());
        Class c=list.getClass();
        try {
            Method method=c.getDeclaredMethod("add", Object.class);
            method.invoke(list,19);
            System.out.println("list.size2: "+list.size()+" value: "+list.toString());

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
