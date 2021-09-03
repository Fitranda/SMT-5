package com.itbrain.catalogmovie.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.widget.SearchView;

import com.itbrain.catalogmovie.R;
import com.itbrain.catalogmovie.adapter.MovieAdapter;
import com.itbrain.catalogmovie.model.Result;
import com.itbrain.catalogmovie.rest.ApiClient;
import com.itbrain.catalogmovie.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private MovieAdapter adapter;
    private SearchView searchView;
    String API_KEY="20f2bfce62bcd57e7152b27052e49941";
    String language = "en-US";
    String CATEGORY = "popular";
    int page = 1;
    RecyclerView recyclerview;
//    List<Result> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerview = findViewById(R.id.rvMovie);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
//        mList = new ArrayList<Result>();
        CallRetrofit();
    }

    private void CallRetrofit() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Response> call = apiInterface.getMovie(CATEGORY,API_KEY,language,page);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, Response<Response> response) {
//
                if (response.isSuccessful() && response.body() != null) {
//                    mList = re
                    List<Result> mList = response.body().getResults();
                    adapter = new MovieAdapter(MainActivity.this,mList);
                    recyclerview.setAdapter(adapter);
                }
//
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() > 1){
                    ApiInterface apiInterface =ApiClient.getClient().create(ApiInterface.class);
                    Call<Response> call = apiInterface.getQuery(API_KEY,language,newText,page);
                    call.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, Response<Response> response) {
                            if (response.isSuccessful() && response.body() != null) {
//                    mList = re
                                List<Result> mList = response.body().getResults();
                                adapter = new MovieAdapter(MainActivity.this,mList);
                                recyclerview.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {

                        }
                    });
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}