package com.chibo.recipoon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import com.chibo.recipoon.Adapter.MyAdapter;
import com.chibo.recipoon.Model.Recipe;
import com.chibo.recipoon.Model.RecipiesResponse;
import com.chibo.recipoon.Retrofit.ApiClient;
import com.chibo.recipoon.Retrofit.ApiInterface;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    //this is what we need.
    private final static String API_KEY = "2defce4ea8f34f178ac1f3ef74cda88d";
    private  String SEARCH = "";
    RecyclerView recyclerView;
    List<Recipe> recipes;
    SearchView searchView;

    ApiInterface apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        searchView.setQueryHint("Search For Recipe");
        searchView.setBackgroundResource(R.drawable.searchview_rounded);


         apiService = ApiClient.getClient().create(ApiInterface.class);



        searchView.setOnQueryTextListener(this);

        }
    private void displayData(RecipiesResponse recipes){
        MyAdapter adapter = new MyAdapter(recipes.getResults(),R.layout.recycler_row,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Call<RecipiesResponse> call = apiService.getResults(API_KEY,SEARCH);
        call.enqueue(new Callback<RecipiesResponse>() {
            @Override
            public void onResponse(Call<RecipiesResponse>call, Response<RecipiesResponse> response) {
                int statusCode = response.code();
                assert response.body() != null;
                recipes= response.body().getResults();
                String BaseUri = response.body().getBaseUri();
                for (Recipe recipe: recipes) {
                    recipe.setImage(BaseUri+recipe.getImage());

                }
                Log.d(TAG, "Status Code: " + statusCode);
                Log.d(TAG, "Number of recipes received: " + recipes.size());
                //displayData(response.body);
                recyclerView.setLayoutManager(new LinearLayoutManager( getApplicationContext()));
                recyclerView.setAdapter(new MyAdapter(recipes, R.layout.recycler_row, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<RecipiesResponse> call, Throwable t) {
                Log.e(TAG,t.getMessage());
            }

        });


        UIUtil.hideKeyboard(this);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        SEARCH = newText;
        return true;
    }
}
