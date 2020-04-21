package com.example.butymovamoneytracker.data;

import com.example.butymovamoneytracker.data.model.AddResult;
import com.example.butymovamoneytracker.data.model.AuthResult;
import com.example.butymovamoneytracker.data.model.BalanceResult;
import com.example.butymovamoneytracker.data.model.Item;
import com.example.butymovamoneytracker.data.model.LogoutResult;
import com.example.butymovamoneytracker.data.model.RemoveResult;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {
    @GET("items")
    Single<List<Item>> items(@Query("type") String type, @Query("auth-token") String auth_token);

    @GET("auth")
    Single<AuthResult> auth(@Query("social_user_id") String social_user_id);

    @GET("logout")
    Single<LogoutResult> logout();

    @POST("items/add")
    Single<AddResult> add(@Body Item item, @Query("auth-token") String auth_token);

    @POST("items/remove")
    Single<RemoveResult> remove(@Query("id") long id, @Query("auth-token") String auth_token);

    @GET("balance")
    Single<BalanceResult> balance();
}
