package com.example.jinbolx.retrofitdemo;

public class Bean {
    private String value;
    private int num;
    public void setValue(String value,int num){
        this.value=value;
        this.num=num;
        System.out.println("value: "+value+" num: "+num);
    }
    private void getValue(){
        System.out.println("getValue: "+value+" getNum: "+num);
    }
}
