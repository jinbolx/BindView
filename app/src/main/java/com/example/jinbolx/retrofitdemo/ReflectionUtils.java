package com.example.jinbolx.retrofitdemo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {

    public static void init() {
        loadClass();
    }

    private static void loadClass() {
        try {
            Class class0 = ThreadUtils.class;
            Class class1 = class0.getClass();
            Class class2 = Class.forName("com.example.jinbolx.retrofitdemo.ThreadUtils");
            ThreadUtils threadUtils = new ThreadUtils();
            Class class3 = threadUtils.getClass();
            System.out.println("class0: " + class0.getName());
            System.out.println("class1: " + class1.getName());
            System.out.println("class2: " + class2.getName());
            System.out.println("class3: " + class3.getName());
            Class class4 = Class.forName("com.example.jinbolx.retrofitdemo.ResonseEntity");
            Object o = class4.newInstance();

            Method method = class4.getMethod("setReason", String.class);
            method.invoke(o, "reson");
            Method[] methods = class4.getDeclaredMethods();
            Method[] publicMethods = class4.getMethods();
            Field[] fields = class4.getDeclaredFields();
            Field[] publicFields = class4.getFields();
            Field field = class4.getDeclaredField("error_code");
            field.setAccessible(true);
            field.set(o, 10);
            int b = field.getInt(o);

            //  int a= (int) field.get(int.class);
            System.out.println("field: " + b);
            Constructor[] publicConstructors = class4.getConstructors();
            Constructor[] constructors = class4.getDeclaredConstructors();
            Constructor singleConstructor = class4.getDeclaredConstructor(int.class);
            singleConstructor.newInstance(6);

            for (Constructor c :
                    publicConstructors) {
                String name = c.getName();
                Class[] params = c.getParameterTypes();
                System.out.println("publicConstructorName: " + name);
                for (Class cl :
                        params) {
                    String n = cl.getName();
                    System.out.println("publicConstructorParamType: " + n);
                }

            }
            for (Constructor c :
                    constructors) {
                String name = c.getName();
                Class[] params = c.getParameterTypes();
                System.out.println("constructorName: " + name);
                for (Class cl :
                        params) {
                    String n = cl.getName();
                    System.out.println("constructorParamType: " + n);
                }
            }
            for (Field field1 : fields) {
                field1.setAccessible(true);
                String name = field1.getName();
                Class type = field1.getType();
                System.out.println("name: " + name + " type: " + type.getName());
            }
            for (Field f :
                    publicFields) {
                f.setAccessible(true);
                String name = f.getName();
                Class type = f.getType();
                System.out.println("publicName: " + name + " type: " + type.getName());
            }
            for (Method m :
                    methods) {
                String methodName = m.getName();
                Class type = m.getReturnType();

                // System.out.println("method: " + methodName + " type: " + type.getName());
            }
            for (Method m :
                    publicMethods) {
                String methodname = m.getName();
                // System.out.println("publicMethod: " + methodname);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
