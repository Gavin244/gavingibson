package com.ggexpress.gavin.options;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity ;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ggexpress.gavin.backend.BackendServer;
import com.ggexpress.gavin.R;
import com.ggexpress.gavin.entites.Frequently;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class HelpCenterActivity extends AppCompatActivity {
    AsyncHttpClient client;
    ArrayList<Frequently> questions;
    ListView queriesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
        BackendServer backend = new BackendServer(this);
        client = backend.getHTTPClient();
        questions = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFrequentlyQuestions();
        queriesList = findViewById(R.id.queries_list);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getFrequentlyQuestions() {
        client.get(BackendServer.url+"/api/ecommerce/frequentlyQuestions/", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Frequently frequently = new Frequently(object);
                        questions.add(frequently);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("JSONObject", "Json parsing error: " + e.getMessage());
                    }
                }
                QueriesAdapter adapter = new QueriesAdapter();
                queriesList.setAdapter(adapter);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private class QueriesAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return questions.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            View v = getLayoutInflater().inflate(R.layout.layout_queries_list, viewGroup, false);
            TextView textQues = v.findViewById(R.id.text_ques);
            TextView textAns = v.findViewById(R.id.text_ans);

            Frequently frequently = questions.get(position);

            textQues.setText("Q. "+frequently.getQuestions());
            textAns.setText("A. "+frequently.getAnswer());

            return v;
        }
    }
}
