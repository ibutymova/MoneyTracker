package com.example.butymovamoneytracker.data;

import com.example.butymovamoneytracker.BuildConfig;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiInitializer {
    private static final String BASE_URL = "https://loftschool.com/android-api/basic/v1/";

    public ApiInitializer() {
    }

    public Api initApi(){
        Gson gson = createGson();
        OkHttpClient client = createOkHttpClient();
        Retrofit retrofit = createRetrofit(gson, client);
        return createApi(retrofit);
    }

    private Gson createGson(){
        return new Gson().newBuilder().create();
    }

    private OkHttpClient createOkHttpClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    private Retrofit createRetrofit(Gson gson, OkHttpClient client){
        return  new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private Api createApi(Retrofit retrofit){
        return retrofit.create(Api.class);
    }
}
