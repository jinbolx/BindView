package com.example.jinbolx.retrofitdemo;

import android.util.Log;

public class Generics<T> {

    private T key;
    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }

    public T show(Generics<T> generics) {

        return generics.key;
    }

    public <E> E show(Generics<E> generics, int a) {
        return generics.getKey();
    }

    public void print(T... args) {
        for (T t : args) {
            Log.i("generics", "print: " + t);
        }
    }
    public static <T> void staticMethod(Generics<T> generics){}
    public void showFun(Generics<? extends Number> generics){
        Log.i("generics","showFun: "+generics.getKey());
    }
    public void showfun2(Generics<? super Dog> generics){

    }
    public class Animal{}
    public class Dog extends Animal{}
}
