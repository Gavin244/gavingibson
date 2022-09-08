package com.ggexpress.gavin.options;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ggexpress.gavin.backend.BackendServer;
import com.ggexpress.gavin.R;
import com.ggexpress.gavin.entites.Product;
import com.ggexpress.gavin.fragments.AllItemsShowActivity;
import com.ggexpress.gavin.product.ItemDetailsActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchResultActivity extends AppCompatActivity {
    AsyncHttpClient client;
    ArrayList<Product> products;
    ListView searchList;
    TextView searchResult;
    Menu menu;
    ProductList productList;
    private String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BackendServer backend = new BackendServer(SearchResultActivity.this);
        client = backend.getHTTPClient();
        products = new ArrayList<>();

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_all_items);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        toggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_white_24dp, getApplicationContext().getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchResult = findViewById(R.id.search_result);
        searchList = findViewById(R.id.search_list);
        if(products.size() != 0) {
            searchResult.setVisibility(View.GONE);
        } else {
            searchResult.setVisibility(View.VISIBLE);
        }
        productList = new ProductList();
        searchList.setAdapter(productList);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.getItem(0);
        // Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//                (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(true );
        searchItem.expandActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.toLowerCase();
                products.clear();
                productList.clearData();
                client.get(BackendServer.url + "/api/ecommerce/searchProduct/?limit=10&search="+query, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                Product userMeta = new Product(object);
                                products.add(userMeta);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("JSONObject", "Json parsing error: " + e.getMessage());
                            }
                        }
                        productList.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(SearchResultActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchResult.setVisibility(View.GONE);
                if(s.equals("")){
                    searchResult.setVisibility(View.VISIBLE);
                    products.clear();
                    productList.clearData();
                } else {
                    s = s.toLowerCase();
                    products.clear();
                    productList.clearData();
                    client.get(BackendServer.url + "/api/ecommerce/searchProduct/?limit=10&search="+s, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject object = response.getJSONObject(i);
                                    Product userMeta = new Product(object);
                                    products.add(userMeta);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("JSONObject", "Json parsing error: " + e.getMessage());
                                }
                            }
                            productList.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            Toast.makeText(SearchResultActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return false;
            }
        });
        return true;
    }
    
    private class ProductList extends BaseAdapter{
        @Override
        public int getCount() {
            return products.size();
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
            View v = getLayoutInflater().inflate(R.layout.layout_search_result_list, viewGroup, false);
            TextView textResult = v.findViewById(R.id.text_result);

            final Product product = products.get(position);
            textResult.setText(product.getName());
            textResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String type = product.getType();
                    if (type.equals("generic")) {
                        startActivity(new Intent(getApplicationContext(), AllItemsShowActivity.class)
                                .putExtra("GY", product.getGY())
                                .putExtra("fragmentName", product.getName().toUpperCase()));
                    } else {
                        if (type.equals("list")) {
                            Intent intent = new Intent(getApplicationContext(), ItemDetailsActivity.class);
//                            intent.putExtra(STRING_IMAGE_URI, cart.getListingParent().getFilesAttachment());
//                            intent.putExtra(STRING_IMAGE_POSITION, position);
                            intent.putExtra("listingLiteGY", product.getGY());
                            startActivity(intent);
                        }
                    }
                }
            });
            return v;
        }

        public void clearData() {
            products.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}