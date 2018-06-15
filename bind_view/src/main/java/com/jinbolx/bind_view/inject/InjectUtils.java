package com.jinbolx.bind_view.inject;

import android.app.Activity;
import android.view.View;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InjectUtils {

    private final static String FIND_VIEW = "findViewById";
    private final static String SET_CONTENT_VIEW = "setContentView";

    public static void inject(Activity activity) {
        try {
            injectContentView(activity);
            injectView(activity);
            injectListener(activity);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private static void injectContentView(Activity activity)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<? extends Activity> c = activity.getClass();
        Method method = c.getMethod(SET_CONTENT_VIEW, int.class);
        method.setAccessible(true);
        boolean hasAnnotation = c.isAnnotationPresent(BindContentView.class);
        if (hasAnnotation) {
            BindContentView bindContentView = c.getAnnotation(BindContentView.class);
            int layout = bindContentView.value();
            method.invoke(activity, layout);
        }
    }

    private static void injectView(Activity activity)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<? extends Activity> c = activity.getClass();
        Field[] fields = c.getDeclaredFields();
        for (Field f :
                fields) {
            boolean hasAnnotation = f.isAnnotationPresent(BindView.class);
            if (hasAnnotation) {
                BindView bindView = f.getAnnotation(BindView.class);
                int value = bindView.value();
                Method method = c.getMethod(FIND_VIEW, int.class);
                method.setAccessible(true);
                Object resView = method.invoke(activity, value);
                f.setAccessible(true);
                f.set(activity, resView);
            }
        }
    }


    private static void injectListener(Activity activity) {
        Class<? extends Activity> c = activity.getClass();
        Method[] methods = c.getMethods();
        for (Method m :
                methods) {
            boolean hasOnclickAnnotation = m.isAnnotationPresent(Onclick.class);
            if (hasOnclickAnnotation) {
                Onclick onclick = m.getAnnotation(Onclick.class);
                String methodName = m.getName();
                OnListener eventListener = new OnListener(activity);
                int[] value = onclick.value();
                for (int v : value) {
                    View view = activity.findViewById(v);
                    eventListener.setOnclick(v, methodName);
                    view.setOnClickListener(eventListener);
                }
            }
        }
    }
}
