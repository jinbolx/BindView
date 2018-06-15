package com.jinbolx.bind_view.inject;

import android.app.Activity;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OnListener implements OnClickListener {

    private Activity activity;
    private static SparseArray<String> listenerMap = new SparseArray<>();

    OnListener(Activity activity) {
        this.activity = activity;
    }

    public void setOnclick(int id, String name) {
        listenerMap.put(id, name);
    }

    public SparseArray<String> getListenerMap() {
        return listenerMap;
    }

    @Override
    public void onClick(View v) {
        if (listenerMap == null || listenerMap.size() == 0) {
            return;
        }
        String methodName = listenerMap.get(v.getId());
        if (TextUtils.isEmpty(methodName)) {
            return;
        }
        try {
            Class<? extends Activity> c = activity.getClass();
            Method method = c.getDeclaredMethod(methodName, View.class);
            method.setAccessible(true);
            method.invoke(activity, v);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
