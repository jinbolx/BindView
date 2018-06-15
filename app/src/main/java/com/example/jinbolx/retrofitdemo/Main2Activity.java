package com.example.jinbolx.retrofitdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class Main2Activity extends AppCompatActivity {

    Disposable disposable;
    ObservableEmitter<Integer> emitte;
    Subscription subscription;
    @BindView(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        log(System.currentTimeMillis() + "");
        // getData();
        //test();
//        Generics<Integer> generics = new Generics<>();
//        generics.setKey(1);
//        generics.setValue(2);
//        showValue(generics);
//        generics.print(1, 2, 3);
//        generics.showFun(generics);
//        System.out.println("key: " + generics.getKey() + " value: " + generics.getValue());
        // mapTest();
        //zipTest();
        //backPressureTest();
        //flowableTest();
        findViewById(R.id.button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                flowableApply();
            }
        });
        findViewById(R.id.button2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                subscription.request(95);
            }
        });
        fileTest();
    }

    public void showValue(Generics<?> obj) {
        log("showValue: " + obj.getKey());
    }

    public void getData() {
//        JokeService service = ThisApplication.retrofitInstance.create(JokeService.class);
//        Call<Object> call = service.joke();
//        call.enqueue(new Callback<Object>() {
//            @Override
//            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
//                Log.i("main2Activity", "response: " + response.body());
//            }
//
//            @Override
//            public void onFailure(Call<Object> call, Throwable t) {
//                Log.i("main2Activity", "error: " + t.getMessage());
//
//            }
//        });
      /*  JokeService jokeService = ThisApplication.retrofitInstance.create(JokeService.class);
        jokeService.jokeRx().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResonseEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i("main2Activity", "onSubscribe: ");
                    }

                    @Override
                    public void onNext(ResonseEntity o) {
                        ResonseEntity resonseEntity = o;
                        Log.i("main2Activity", "response: " + resonseEntity);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("main2Activity", "error: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.i("main2Activity", "onComplete: ");
                    }
                });*/
        final JokeService jokeService = ThisApplication.retrofitInstance.create(JokeService.class);
        Disposable disposable = jokeService.jokeRx().subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<ResonseEntity>() {
                    @Override
                    public void accept(ResonseEntity resonseEntity) throws Exception {
                        log("rx: accept: " + resonseEntity.getResult());
                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        log("rx: error: " + throwable.getMessage());
                    }
                })
                .flatMap(new Function<ResonseEntity, ObservableSource<ResonseEntity>>() {
                    @Override
                    public ObservableSource<ResonseEntity> apply(ResonseEntity resonseEntity)
                            throws Exception {
                        log("rx: " + resonseEntity.getResult());
                        if (resonseEntity.getResult() != null) {
                            return jokeService.jokeNext();
                        } else {
                            return null;
                        }
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResonseEntity>() {
                    @Override
                    public void accept(ResonseEntity resonseEntity) throws Exception {
                        log("next: " + resonseEntity.getResult());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        log("next: " + throwable.getMessage());
                    }
                });

    }

    public void test() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                log("subscribe: " + Thread.currentThread().getName());
                for (int i = 0; i < 3; i++) {
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        });
        Observer<Integer> observer = new Observer<Integer>() {
            private Disposable mDisposable;

            @Override
            public void onSubscribe(Disposable d) {
                log("onSubscribe");
                mDisposable = d;
            }

            @Override
            public void onNext(Integer integer) {
                log("onNext: " + integer.toString());
                log("onNext: " + Thread.currentThread().getName());
                if (integer == 3) {
                    mDisposable.dispose();
                }
                log("isDisposed: " + mDisposable.isDisposed());
            }

            @Override
            public void onError(Throwable e) {
                log(e.getMessage());
            }

            @Override
            public void onComplete() {
                log("onComplete");
            }
        };
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        log("accept1: " + integer + " thread: " + Thread.currentThread().getName());
                    }
                })
                .observeOn(Schedulers.computation())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        log("accept2: " + integer + " thread: " + Thread.currentThread().getName());
                    }
                })
                .observeOn(Schedulers.newThread())
                .subscribe(observer);
        CompositeDisposable disposable = new CompositeDisposable();
        Disposable disposable1 = observable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                log("accept: " + integer);
            }
        });
        disposable1.dispose();
        log(Thread.currentThread().getName());

    }

    public void mapTest() {
//        Disposable disposable = Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                for (int i = 0; i < 3; i++) {
//                    emitter.onNext(i);
//                }
//            }
//        }).subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(Schedulers.io())
//                .map(new Function<Integer, Entity>() {
//                    @Override
//                    public Entity apply(Integer integer) throws Exception {
//                        List<String> list = new ArrayList<>();
//                        for (int i = 1; i < 4; i++) {
//                            list.add(i + "");
//                        }
//                        Entity entity = new Entity();
//                        entity.setId(integer);
//                        entity.setList(list);
//                        log("map: " + Thread.currentThread().getName());
//                        return entity;
//                    }
//                })
//                /*.concatMap(new Function<Integer, ObservableSource<Entity>>() {
//                    @Override
//                    public ObservableSource<Entity> apply(Integer integer) throws Exception {
//                        List<String> list= new ArrayList<>();
//                        for (int i = 1; i < 4; i++) {
//                                list.add(i+"");
//                        }
//                        Entity entity=new Entity();
//                        entity.setId(integer);
//                        entity.setList(list);
//                        return entity;
//                    }
//
//                })*/.subscribe(new Consumer<Entity>() {
//                    @Override
//                    public void accept(Entity s) throws Exception {
//                        log("id: " + s.getId() + "");
//                        log("accept: " + Thread.currentThread().getName());
//                    }
//                });
        Disposable disposable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 2; i++) {
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        return integer * 5;
                    }
                }).flatMap(
                        new Function<Integer, ObservableSource<String>>() {
                            @Override
                            public ObservableSource<String> apply(Integer integer)
                                    throws Exception {
                                List<String> list = new ArrayList<>();
                                for (int i = 0; i < 2; i++) {
                                    list.add(i + " integer: " + integer);
                                }
                                return Observable.fromIterable(list);
                            }
                        }).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        log("flatMap: " + s);
                    }
                });


    }

    public void zipTest() {
        Observable<Integer> observable0 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 3; i++) {

                    Thread.sleep(1000);
                    log("observable0: " + i + " thread: " + Thread.currentThread().getName());
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.computation());
        Observable<String> observable1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                for (int i = 0; i < 2; i++) {
                    Thread.sleep(500);
                    log("observable1: number" + i + " thread: " + Thread.currentThread().getName());
                    emitter.onNext("number" + i);
                }
                Thread.sleep(3000);
                log("observable1: number" + 2 + " thread: " + Thread.currentThread().getName());
                emitter.onNext("number" + 2);
                Thread.sleep(3000);
                log("observable1: number" + 3 + " thread: " + Thread.currentThread().getName());
                emitter.onNext("number" + 3);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());
        Disposable disposable = Observable.zip(observable0,
                observable1, new BiFunction<Integer, String, String>() {
                    @Override
                    public String apply(Integer integer, String s) throws Exception {
                        String str = integer + s;
                        log("apply: " + str + " thread: " + Thread.currentThread().getName());
                        return str;
                    }
                }
        ).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                log("accept: " + s + " thread: " + Thread.currentThread().getName());
            }
        });
    }

    public void backPressureTest() {
        disposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitte = emitter;
                for (int i = 0; ; i++) {
                    log("backpressure: sub " + i);
                    emitter.onNext(i);
                    Thread.sleep(2000);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        log("backpressure: accept " + integer);
                    }
                });
//        disposable= Observable.fromCallable(new Callable<Integer>() {
//            @Override
//            public Integer call() throws Exception {
//                for (int i = 0; ; i++) {
//                    log("backpressure: sub "+i);
//                    return i;
//
//                }
//
//            }
//        }).filter(new Predicate<Integer>() {
//            @Override
//            public boolean test(Integer integer) throws Exception {
//                return integer%100000==0;
//            }
//        }).subscribeOn(Schedulers.io()).subscribe(new Consumer<Integer>() {
//            @Override
//            public void accept(Integer integer) throws Exception {
//                log("backpressure: accept "+integer);
//            }
//        });
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io()).sample(1, TimeUnit.SECONDS);
        Observable<String> observable1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("a");
                emitter.onNext("b");
                emitter.onNext("c");
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable, observable1, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                log("backpressure accept: " + s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                log("backpressure throwable: " + throwable.getMessage());
            }
        }).dispose();
    }

    public void flowableTest() {
        Flowable<Integer> flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 228; i++) {
                    log("flowable: " + i);
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()).observeOn(Schedulers.io());
        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                log("subscriber: " + s);
                // s.request(Long.MAX_VALUE);
                s.request(100);
            }

            @Override
            public void onNext(Integer s) {
                log("onNext: " + s);
            }

            @Override
            public void onError(Throwable t) {
                log("onError: " + t);
            }

            @Override
            public void onComplete() {

            }
        };
        flowable
//                .map(new Function<Integer, String>() {
//            @Override
//            public String apply(Integer integer) throws Exception {
//                return integer+"";
//            }
//        })
                .subscribe(subscriber);


    }

    public void flowableTest2() {
//        Flowable.create(new FlowableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
//                for (int i = 0; i<10000; i++) {
//                    log("subscribe: " + i);
//                    emitter.onNext(i);
//                }
//            }
//        }, BackpressureStrategy.LATEST).subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribe(new Subscriber<Integer>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        log("subscription: " + s);
//                        s.request(128);
//                        subscription = s;
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        log("onNext: " + integer);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        log("error: " + t.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        log("onComplete");
//                    }
//                });

        Flowable.interval(1,TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .onBackpressureLatest()
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription=s;
                        log("subscribe");
                        s.request(128);

                    }

                    @Override
                    public void onNext(Long aLong) {
                        log("onnext: "+aLong);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        log("onError: "+t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void flowableTest3(){
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {

//                for (int i = 0; i < 10; i++) {
//                    log("emitter: "+emitter.requested());
//                    emitter.onNext(i);
//                }
                log("emitter: "+emitter.requested());
                boolean flag;
                for (int i = 0; ; i++) {
                    flag=false;
                    while (emitter.requested()==0){
                        if (!flag){
                            log("emitter: cant emit");
                            flag=true;
                        }
                    }
                    emitter.onNext(i);
                    log("emitter: "+i+",requested: "+emitter.requested());
                }

            }
        },BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io()).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                log("onSubscribe: "+s);
               // s.request(5);
                subscription=s;
               // s.request(3);
            }

            @Override
            public void onNext(Integer integer) {
                log("onNext: "+integer);
            }

            @Override
            public void onError(Throwable t) {
                log("onError: "+t);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void flowableApply(){
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                InputStream inputStream=getApplicationContext().getAssets().open("test.txt");
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(inputStreamReader);
                String str;

                while ((str = br.readLine()) != null && !emitter.isCancelled()) {
                    while (emitter.requested() == 0) {
                        if (emitter.isCancelled()) {
                            break;
                        }
                    }
                    emitter.onNext(str);
                }

                br.close();
                inputStreamReader.close();
                inputStream.close();
                emitter.onComplete();
            }
        },BackpressureStrategy.ERROR).observeOn(Schedulers.io()).subscribe(
                new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        log("onSub: "+s);
                        subscription=s;
                    }

                    @Override
                    public void onNext(String s) {
                        log("onNext: "+s);
                        try {
                            Thread.sleep(1000);
                            subscription.request(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        log("onError: "+t);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public void fileTest(){
        File file=new File(FileUtil.getAppPath());
        if (!file.exists()){
            boolean b= file.mkdirs();
            log("mkdir: "+b+" file:  "+file.getAbsolutePath());
        }
        String text="test.txt";
        final File textFile=new File(file,text);
        if (!textFile.exists()) {
            try {
             boolean result=   textFile.createNewFile();
             log("createFile: "+result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            OutputStream outputStream=new FileOutputStream(textFile);
            String str="hello world"
                    + "nihao"
                    + "nihaoma"
                    + "hihao";
            byte b[]=str.getBytes();
           // outputStream.write(b);
            Writer writer=new FileWriter(textFile);
            writer.write(str);
            //writer.close();
            writer.flush();
           // outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Flowable.create(new FlowableOnSubscribe<String>() {
//            @Override
//            public void subscribe(FlowableEmitter<String> emitter) throws Exception {
//                //InputStream inputStream= new FileInputStream(textFile.getAbsoluteFile());
//                InputStream inputStream= getAssets().open("test.txt");
//                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
//                BufferedReader br = new BufferedReader(inputStreamReader);
//                String str;
//
//                while ((str = br.readLine()) != null && !emitter.isCancelled()) {
//                    while (emitter.requested() == 0) {
//                        if (emitter.isCancelled()) {
//                            break;
//                        }
//                    }
//                    emitter.onNext(str);
//                }
//
//                br.close();
//                inputStreamReader.close();
//                inputStream.close();
//                emitter.onComplete();
//            }
//        },BackpressureStrategy.BUFFER).observeOn(Schedulers.io()).subscribe(
//                new Subscriber<String>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        log("onSub: "+s);
//                        subscription=s;
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        log("onNext: "+s);
//                        try {
//                            Thread.sleep(1000);
//                            subscription.request(1);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        log("onError: "+t);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        log("onComplete");
//                    }
//                });
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                InputStream inputStream= new FileInputStream(textFile.getAbsoluteFile());
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(inputStreamReader);
                String str;

                while ((str = br.readLine()) != null ) {
//                    while (emitter.requested() == 0) {
//                        if (emitter.isCancelled()) {
//                            break;
//                        }
//                    }
                    emitter.onNext(str);
                }

                br.close();
                inputStreamReader.close();
                inputStream.close();
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                    log("onNext: "+s);
                    }

                    @Override
                    public void onError(Throwable e) {
                    log("onError: "+e);
                    }

                    @Override
                    public void onComplete() {
                        log("onComplete");
                    }
                });
    }

    void log(String s) {
        Log.i("Main2Activity", s);
    }

    public <T> T genericsMethod(T t) {
        return t;
    }
}
