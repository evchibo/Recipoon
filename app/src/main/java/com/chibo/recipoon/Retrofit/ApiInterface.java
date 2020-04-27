package com.chibo.recipoon.Retrofit;

import com.chibo.recipoon.Model.InstructionResponse;
import com.chibo.recipoon.Model.RecipiesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    //@define chibo
    //To enter a locked house you need a key...of breaking into a house without authorization is a sin
    //This is our interface class for apikey that's appended to every query we're going to make
    @GET("search")
    Call<RecipiesResponse> getResults(@Query("apiKey") String apiKey
            , @Query("query") String sample);
    @GET("{id}/analyzedInstructions")
    Call<List<InstructionResponse>> getInstructions(@Path("id") int id, @Query("apiKey") String apiKey);


}
