package com.example.aureliengiudici.ethrade.NetworkManager;

/**
 * Created by aureliengiudici on 17/02/2018.
 */

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface IRequestInterface {
    @POST("us")
    Call<ServerResponse> operation(@Body ServerRequest request);
}
