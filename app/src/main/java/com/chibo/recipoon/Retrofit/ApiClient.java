package com.chibo.recipoon.Retrofit;



import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//chibo@engineer.com
public class ApiClient {
    //This class is for defining our backend base url...who doesn't love spoon and fork...hey look it's spoonacular
    private static final String BASE_URL = " https://api.spoonacular.com/recipes/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
