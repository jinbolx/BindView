package com.example.jinbolx.retrofitdemo;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface JokeService {

    @GET("list.php?key=f239dd7b9d1913f94268c4f95134f2b4&page=2&pagesize=10&sort=asc&time=1418745237")
    Call<Object> joke();

    @GET("list.php?key=f239dd7b9d1913f94268c4f95134f2b4&page=2&pagesize=10&sort=asc&time=141874523711111")
    Observable<ResonseEntity> jokeRx();
    @GET("list.php?key=f239dd7b9d1913f94268c4f95134f2b4&page=2&pagesize=10&sort=asc&time=1418745237")
    Observable<ResonseEntity> jokeNext();
}
