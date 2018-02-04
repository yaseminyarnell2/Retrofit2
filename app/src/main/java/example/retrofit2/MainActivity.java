package example.retrofit2;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import example.retrofit2.data.model.Item;
import example.retrofit2.data.model.remote.ApiUtils;
import example.retrofit2.data.model.remote.SOAnswersResponse;
import example.retrofit2.data.model.remote.SOService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;






public class MainActivity extends Activity {

    private RecyclerView mRecyclerView;
    SOService mService;
    private AnswersAdapter mAdapter;

    protected void onCreate (Bundle savedInstanceState)  {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_main );
        mService = ApiUtils.getSOService();
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_answers);
        mAdapter = new AnswersAdapter(this, new ArrayList<Item>(0), new AnswersAdapter.PostItemListener() {

            @Override
            public void onPostClick(long id) {
                Toast.makeText(MainActivity.this, "Post id is" + id, Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        loadAnswers();
    }


   /* public Call<SOAnswersResponse> getAnswers() {
        Call<SOAnswersResponse> answers;
        return answers;
    }*/



    public void loadAnswers() {
      mService.getAnswers().enqueue(new Callback<SOAnswersResponse>() {
            @Override
            public void onResponse(Call<SOAnswersResponse> call, Response<SOAnswersResponse> response) {

                if(response.isSuccessful()) {
                    mAdapter.updateAnswers(response.body().getItems());
                    Log.d("MainActivity", "posts loaded from API");
                }else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<SOAnswersResponse> call, Throwable t) {
                showErrorMessage();
                Log.d("MainActivity", "error loading from API");

            }


        });
    }

    private void showErrorMessage() {
        Log.d("MainActivity", "error loading from API");
    }
}


