package com.chibo.recipoon;

import android.os.Bundle;

import com.chibo.recipoon.Adapter.InstructionAdapter;
import com.chibo.recipoon.Model.InstructionResponse;
import com.chibo.recipoon.Retrofit.ApiClient;
import com.chibo.recipoon.Retrofit.ApiInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class RecipeDetailsActivity extends AppCompatActivity {
    private TextToSpeech textToSpeech;
    private static final String TAG = "RecipeDetailsActivity";
    private final static String API_KEY = "2defce4ea8f34f178ac1f3ef74cda88d";
    ImageView imageView;
    RecyclerView recyclerView;
    //List<Step> steps;
    List<InstructionResponse> steps;


    ApiInterface apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        imageView = findViewById(R.id.image);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.stepList);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Enqueueing all Steps to Speak Engine", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                speak();
            }
        });
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result ==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e(TAG, "onInit: Language not supported" );
                    }else {
                        fab.setEnabled(true);
                    }
                }else {
                    Log.e(TAG, "onInit: Initialization Failed" );
                }
            }
        });


        apiService = ApiClient.getClient().create(ApiInterface.class);
        getIncomingIntent();



    }

    private void speak(){
        String toSpeak ;
        String stepNumber;
        for (int i =0;i<steps.get(0).getSteps().size();i++){
            stepNumber = "Step "+steps.get(0).getSteps().get(i).getNumber();
            textToSpeech.speak(stepNumber,TextToSpeech.QUEUE_ADD,null,null);
            textToSpeech.playSilentUtterance(2000, TextToSpeech.QUEUE_ADD, null);
            toSpeak = steps.get(0).getSteps().get(i).getStep();
            textToSpeech.speak(toSpeak,TextToSpeech.QUEUE_ADD,null,null);
            textToSpeech.playSilentUtterance(3000, TextToSpeech.QUEUE_ADD, null);
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private void getIncomingIntent(){
        Log.d(TAG,"getIncomingIntent: Checking for incomin intents");
        if (getIntent().hasExtra("title") && getIntent().hasExtra("servings")){
            Log.d(TAG, "getIncomingIntent: found intent extras");

            String title = getIntent().getStringExtra("title");
            getSupportActionBar().setTitle(title);
            String image = getIntent().getStringExtra("image");
            String servings = getIntent().getStringExtra("servings");
            int id = getIntent().getIntExtra("id",0);
            setRecipe(title,image,servings);
            getInstructionsForRecipe(id);





        }
    }

    private void getInstructionsForRecipe(int id) {
        Call <List<InstructionResponse>> call = apiService.getInstructions(id,API_KEY);
        call.enqueue(new Callback<List<InstructionResponse>>() {
            @Override
            public void onResponse(Call <List<InstructionResponse>> call, Response<List<InstructionResponse>> response) {
                int statusCode = response.code();
                assert response.body() != null;
                steps = response.body();
                Log.d(TAG, "Status Code: " + statusCode);
                Log.d(TAG, "Number of steps received: " + steps.get(0).getSteps().size());

                for (int i =0;i<steps.get(0).getSteps().size();i++){
                    Log.d(TAG, "Step: Number "+steps.get(0).getSteps().get(i).getNumber()+" " +steps.get(0).getSteps().get(i).getStep());
                }
                recyclerView.setLayoutManager(new LinearLayoutManager( getApplicationContext()));
                recyclerView.setAdapter(new InstructionAdapter(steps.get(0).getSteps(), R.layout.steplist_row, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<List<InstructionResponse>> call, Throwable t) {

                Log.e(TAG, "onFailure: "+ t.getMessage());
            }
        });
    }

    private void setRecipe(String title,String image, String servings){

        TextView name = findViewById(R.id.recipe_title);
        name.setText(title);
        TextView servings_amount = findViewById(R.id.recipe_servings);
        servings_amount.setText("Servings:"+servings);


        Picasso.get().load(image).resize(300,300).centerCrop().into(imageView);

    }
}
