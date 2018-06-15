package com.example.jinbolx.retrofitdemo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadUtils {
    private static volatile boolean flag=false;
    private static  volatile int count=0;
   static Lock lock=new ReentrantLock();
    public static void init() throws InterruptedException {
//        try {
//            startCallableTask();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
     // startThread();
//        Thread thread1=new Thread(new MyRunnable());
//        thread1.start();
        Thread thread=new MyThread2();
        thread.start();
        //thread.interrupt();
        //Thread.sleep(1000);
        System.out.println("threadName: "+thread.getName()+" id: "+thread.getId()+" isalive: "+thread.isAlive()+" isinterrupt: "+thread.isInterrupted());
        //startJoin();
       // syncMethod();
       // syncTest();
        volatileTest();
        volatileTestMethod();
    }
    static void startThread(){
        MyThread thread0 = new MyThread();
        Thread thread = new Thread(thread0, "A");
        Thread thread1 = new Thread(thread0, "B");
        Thread thread2 = new Thread(thread0, "c");
        Thread thread3 = new Thread(thread0, "D");
        Thread thread4 = new Thread(thread0, "E");
        thread.start();
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        System.out.println("end");
    }
    static class MyThread extends Thread {

        private int count = 300;

        @Override
        public  void run() {
            super.run();
            System.out.println("thread run");
            while (count > 0) {
                count--;
                System.out.println(MyThread.currentThread().getName() + " count: " + count);
            }
        }
    }

    static class MyRunnable implements Runnable {

        @Override
        public void run() {
            System.out.println("runnable run");
        }
    }
    private static void startCallableTask() throws ExecutionException, InterruptedException {
        System.out.println("----begin run -------");
        Date date0=new Date();
        int taskSize=10;
        ExecutorService executorService= Executors.newFixedThreadPool(taskSize);
        List<Future> list=new ArrayList<>();
        for (int i = 0; i < taskSize; i++) {
            Callable callable=new MyCallable(i+"");
            Future f=executorService.submit(callable);
            list.add(f);
        }
        executorService.shutdown();
        for (Future f :
                list) {
            System.out.println(">>>"+f.get().toString());
            Date date1=new Date();
            System.out.println("---end---,during="+(date1.getTime()-date0.getTime())+"ms");
        }
    }
    static class MyCallable implements Callable<Object>{
        private String taskNum;

        public MyCallable(String taskNum) {
            this.taskNum = taskNum;
        }

        @Override
        public Object call() throws Exception {
            System.out.println(">>>"+taskNum+"start");
            Date date0=new Date();
            Thread.sleep(100);
            Date date1=new Date();
            long during=date1.getTime()-date0.getTime();
            System.out.println(">>>"+taskNum+"end");
            return taskNum+"任务返回结果，当前任务时间"+during+"ms";
        }
    }
    static class MyThread2 extends Thread{

        @Override
        public void run() {
            super.run();
            long begin=System.currentTimeMillis();
            int count =0;
            for (int i = 0; i < 500000; i++) {
                count=count+i+1;
                //Thread.yield();
            }
            long end=System.currentTimeMillis();
            System.out.println("using: "+(end-begin)+"ms");
        }
    }
    static class MyThread3 extends Thread{

        public MyThread3(String name) {
            super(name);
        }

        @Override
        public void run() {
            super.run();
            for (int i = 0; i < 4; i++) {
                System.out.println(getName()+" "+i);
            }
        }
    }
    private static void startJoin() throws InterruptedException {
        new MyThread3("Thread1").start();
        for (int i = 0; i < 10; i++) {
            if (i==5){
                Thread thread=new MyThread3("join Thread");
                thread.setDaemon(true);
                thread.start();
                thread.join();
            }

            System.out.println(Thread.currentThread().getName()+" "+i);
        }
    }
    static class SyncThread implements Runnable{

        @Override
        public void run() {
            System.out.println("------begin------");
            synchronized (this){
                for (int i = 0; i < 4; i++) {
                    System.out.println(Thread.currentThread().getName()+" syncloop: "+i);
                }
            }
            System.out.println("---------end---------");
        }
    }
    static void syncMethod(){
        SyncThread thread=new SyncThread();
        Thread thread1=new Thread(thread,"a");
        Thread thread2=new Thread(thread,"b");
        thread1.start();
        thread2.start();
    }
    static class Thread3{
        public void test1(){
            synchronized (this){
                for (int i = 0; i < 5; i++) {
                    System.out.println(Thread.currentThread().getName()+i);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        public synchronized void test3(){
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName()+i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        public static synchronized void test4(){
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName()+i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void test2(){
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName()+i);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    public static void syncTest(){
        final Thread3 thread3=new Thread3();
        final Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
               // thread3.test1();
                Thread3.test4();
            }
        },"A");
        Thread thread1=new Thread(new Runnable() {
            @Override
            public void run() {
                //thread3.test3();
               // thread3.test2();
               thread3.test1();
            }
        },"B");
        thread1.start();
        thread.start();
    }
    static class VolatileClass extends Thread{

        @Override
        public void run() {
            super.run();
            while (!flag){
                System.out.println("-----running-----");
            }
            System.out.println("---end----");
        }

    }
    static void volatileTest(){
        VolatileClass volatileClass=new VolatileClass();
        volatileClass.start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        flag=true;
    }
    private static void volatileTestMethod(){
        final VolatileTest test=new VolatileTest();
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j <10000 ; j++) {
                        test.increase2();
                    }
                }
            }).start();
        }
        while (Thread.activeCount()>1)
            Thread.yield();
        System.out.println("count: "+count);
    }
    static class VolatileTest{

        public synchronized void increase() {
            count++;
        }
        public void increase2(){
           // lock.lock();
            count++;
            //lock.unlock();
        }
    }
}
