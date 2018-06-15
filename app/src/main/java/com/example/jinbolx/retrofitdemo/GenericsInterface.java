package com.example.jinbolx.retrofitdemo;

public interface GenericsInterface<T> {
    public T geneMethod();
    class CommonGenerics implements GenericsInterface<Integer>{

        @Override
        public Integer geneMethod() {
            return null;
        }
    }
    class GeneClass<T> implements GenericsInterface<T>{

        @Override
        public T geneMethod() {
            return null;
        }
    }
}
