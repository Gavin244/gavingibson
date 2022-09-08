package com.ggexpress.gavin.product;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import 	androidx.viewpager.widget.ViewPager
        ;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.ggexpress.gavin.backend.BackendServer;
import com.ggexpress.gavin.R;
import com.ggexpress.gavin.entites.ListingLite;
import com.ggexpress.gavin.notification.NotificationCountSetClass;
import com.ggexpress.gavin.options.CartListActivity;
import com.ggexpress.gavin.startup.LoginPageActivity;
import com.ggexpress.gavin.startup.MainActivity;
import com.ggexpress.gavin.startup.SliderImageFragmentAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class ItemDetailsActivity extends AppCompatActivity {
    LinearLayout layoutActionBuy, layoutOutOFStock;
    private SliderImageFragmentAdapter mSliderImageFragmentAdapter;
    private ViewPager mViewPager;
    private ExtensiblePageIndicator extensiblePageIndicator;
    TextView textViewItemName, textViewItemPrice, textViewItemDiscountPrice, textViewItemDiscount;
    Button textViewAddToCart, textViewBuyNow;
    String itemGY;
    Spinner dropdownSpinner;
    RecyclerView suggestedRecyclerView;
    Context mContext;
    private Menu menu;
    AsyncHttpClient client;
    public static ArrayList<ListingLite> listingLites;
    public static ArrayList<ListingLite> suggestList;
    ArrayList spinnerlist = new ArrayList();
    String keys[] = {"str"};
    int ids[] = {android.R.id.text1};
    public static ListingLite lite;
    String name, value, fieldType, helpText, unit, jsonData;
    JSONArray jsonArray = new JSONArray();
    Toast toast;
    String sku;
    ProgressBar progressBar;
    LinearLayout itemDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        mContext = ItemDetailsActivity.this;
        BackendServer backend = new BackendServer(mContext);
        client = backend.getHTTPClient();
        listingLites = new ArrayList<>();
        suggestList = new ArrayList<>();

        if (getIntent() != null) {
            itemGY = getIntent().getStringExtra("listingLiteGY");
        }
        getParentType();

        progressBar = findViewById(R.id.progressBar);
        itemDetails = findViewById(R.id.layout_linear_item_details);
        progressBar.setVisibility(View.VISIBLE);
        itemDetails.setVisibility(View.GONE);

//                specificationsListView.setLayoutManager(new LinearLayoutManager(mContext));
//                SpecificationsListAdapter list = new SpecificationsListAdapter();
//                specificationsListView.setAdapter(list);
//                list.notifyDataSetChanged();

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//                        suggestedRecyclerView.setLayoutManager(layoutManager);
//                        SuggestedRecyclerViewAdapter viewAdapter = new SuggestedRecyclerViewAdapter(suggestList);
//                        suggestedRecyclerView.setAdapter(viewAdapter);
//                    }
//                    },500);

    }

    public void init() {
        String filesAttachment1;
        textViewItemName = findViewById(R.id.item_name);
        dropdownSpinner = findViewById(R.id.item_variants_spinner);
        textViewItemPrice = findViewById(R.id.item_price);
        textViewItemDiscountPrice = findViewById(R.id.actual_price);
        textViewItemDiscount = findViewById(R.id.discount_percentage);
        textViewAddToCart = findViewById(R.id.text_action_add_cart);
        textViewBuyNow = findViewById(R.id.text_action_buy);
        layoutActionBuy = findViewById(R.id.layout_action_buy);
        layoutOutOFStock = findViewById(R.id.out_of_stock_action);
//        specificationsListView = findViewById(R.id.specifications_list);
//        textViewDescriptions = findViewById(R.id.description_txt);
        suggestedRecyclerView = findViewById(R.id.suggested_recyclerview);
        extensiblePageIndicator = findViewById(R.id.flexible_indicator);
        mViewPager = (ViewPager) findViewById(R.id.item_view_pager);
        lite = listingLites.get(0);
        mSliderImageFragmentAdapter = new SliderImageFragmentAdapter(getSupportFragmentManager());
        for (int j = 0; j < lite.getFilesArray().length(); j++) {
            try {
                JSONObject filesObject = lite.getFilesArray().getJSONObject(j);
                String filesAttachment = null;
                filesAttachment = filesObject.getString("attachment");
                if (filesAttachment.equals("null") || filesAttachment.equals("") || filesAttachment == null) {
                    filesAttachment1 = BackendServer.url + "/media/ecommerce/pictureUploads/1532690173_89_admin_ecommerce.jpg";
                } else filesAttachment1 = filesAttachment;
                mSliderImageFragmentAdapter.addFragment(ProductImageFragment.newInstance(android.R.color.transparent, filesAttachment1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        mViewPager.setAdapter(mSliderImageFragmentAdapter);
        extensiblePageIndicator.initViewPager(mViewPager);
        String spinnerstr = lite.getHowMuch()+" "+lite.getUnit();
        textViewItemName.setText(lite.getProductName()+" "+spinnerstr);
        if (lite.getProductDiscount().equals("0")) {
            textViewItemPrice.setText("\u0024" + lite.getProductPrice());
            spinnerstr += " - \u0024"+ lite.getProductPrice();
            textViewItemDiscount.setVisibility(View.GONE);
            textViewItemDiscountPrice.setVisibility(View.GONE);
        } else {
            textViewItemPrice.setText("\u0024" + lite.getProductDiscountedPrice());
            spinnerstr += " - \u0024"+ lite.getProductDiscountedPrice();
            textViewItemDiscount.setVisibility(View.VISIBLE);
            textViewItemDiscount.setText("(" + lite.getProductDiscount() + "% OFF)");
            textViewItemDiscountPrice.setVisibility(View.VISIBLE);
            textViewItemDiscountPrice.setText("\u0024" + lite.getProductPrice());
            textViewItemDiscountPrice.setPaintFlags(textViewItemDiscountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        HashMap map = new HashMap();
        map.put(keys[0], spinnerstr);
        map.put("sku", lite.getSerialNo());
        map.put("disPer", lite.getProductDiscount());
        map.put("discount", lite.getProductIntDiscountedPrice());
        spinnerlist.add(map);
        JSONArray array = lite.getItemArray();
        if (array.length()>0) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = array.getJSONObject(i);
                    String sku = jsonObj.getString("sku");
                    String updated = jsonObj.getString("updated");
                    String unitPerpack = jsonObj.getString("unitPerpack");
                    String created = jsonObj.getString("created");
                    String pricearray = jsonObj.getString("price");
                    String discountedPrice = jsonObj.getString("discountedPrice");
                    String parent_id = jsonObj.getString("parent_id");
                    String id = jsonObj.getString("id");
                    Double rspoint = Double.parseDouble(pricearray);
                    final int rs = (int) Math.round(rspoint);
                    Double rsPointdis = Double.parseDouble(discountedPrice);
                    final int rsdis = (int) Math.round(rsPointdis);
                    String  strvalue = (Double.parseDouble(lite.getHowMuch())*Integer.parseInt(unitPerpack))+" "+ lite.getUnit()+" - \u20B9"+ rs;
                    HashMap map1 = new HashMap();
                    map1.put(keys[0], strvalue);
                    map1.put("sku", sku);
                    map1.put("disPer", lite.getProductDiscount());
                    map1.put("discount", rsdis);
                    spinnerlist.add(map1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        SimpleAdapter adapter = new SimpleAdapter(mContext, spinnerlist, android.R.layout.simple_spinner_dropdown_item, keys, ids);
        dropdownSpinner.setAdapter(adapter);

        dropdownSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HashMap map = (HashMap) spinnerlist.get(position);
                String spinnerValue = (String) map.get(keys[0]);
                sku = (String) map.get("sku");
                String disPer = (String) map.get("disPer");
                int discount = (int) map.get("discount");
                Log.e("onItemClick",(String) map.get("sku")+" "+spinnerValue);
                String arrSplit[] = spinnerValue.split("-");
                if (disPer.equals("0")){
                    textViewItemName.setText(lite.getProductName()+" "+arrSplit[0]);
                    textViewItemPrice.setText(arrSplit[1]);
                    textViewItemDiscount.setVisibility(View.GONE);
                    textViewItemDiscountPrice.setVisibility(View.GONE);
                } else {
                    textViewItemName.setText(lite.getProductName()+" "+arrSplit[0]);
                    textViewItemPrice.setText("\u0024"+discount);
                    textViewItemDiscountPrice.setVisibility(View.VISIBLE);
                    textViewItemDiscountPrice.setText(""+arrSplit[1]);
                    textViewItemDiscountPrice.setPaintFlags(textViewItemDiscountPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                    textViewItemDiscount.setVisibility(View.VISIBLE);
                    textViewItemDiscount.setText(disPer+"% OFF");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        Spanned htmlAsSpanned = Html.fromHtml(lite.getSource());
//        if (htmlAsSpanned.toString().equals("null")|| htmlAsSpanned.toString().equals("")|| htmlAsSpanned.toString() == null) {
//            textViewDescriptions.setText("");
//        } else {
//            textViewDescriptions.setText("\u2022 " +htmlAsSpanned);
//        }

        if (lite.isInStock()) {
            layoutActionBuy.setVisibility(View.VISIBLE);
            layoutOutOFStock.setVisibility(View.GONE);
            String qunt = lite.getAddedCart();
            int qntAdd = Integer.parseInt(qunt);
            if (qntAdd <= 0) {
                textViewAddToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RequestParams params = new RequestParams();
                        params.put("prodSku", sku);
                        params.put("product", lite.getGY());
                        params.put("qty", "1");
                        params.put("type", "card");
                        params.put("user", MainActivity.userGY);
                        client.post(BackendServer.url + "/api/ecommerce/cart/", params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                if (toast != null) {
                                    toast.cancel();
                                }
                                toast = Toast.makeText(ItemDetailsActivity.this, "Item added to cart.", Toast.LENGTH_SHORT);
                                toast.show();
                                MainActivity.notificationCountCart++;
                                NotificationCountSetClass.setNotifyCount(MainActivity.notificationCountCart);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                if (toast != null) {
                                    toast.cancel();
                                }
                                toast = Toast.makeText(mContext, "This Product is already in card.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    }
                });
            } else {
                textViewAddToCart.setText("GO TO CART");
                textViewAddToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(ItemDetailsActivity.this, CartListActivity.class));
                    }
                });
            }

            textViewBuyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RequestParams params = new RequestParams();
                    params.put("prodSku", sku);
                    params.put("product", lite.getGY());
                    params.put("qty", "1");
                    params.put("typ", "cart");
                    params.put("user", MainActivity.userGY);
                    client.post(BackendServer.url + "/api/ecommerce/cart/", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if (toast != null) {
                                toast.cancel();
                            }
                            toast = Toast.makeText(mContext, "Item added to cart.", Toast.LENGTH_SHORT);
                            toast.show();
                            MainActivity.notificationCountCart++;
                            NotificationCountSetClass.setNotifyCount(MainActivity.notificationCountCart);
                            startActivity(new Intent(ItemDetailsActivity.this, CartListActivity.class));
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            if (toast != null) {
                                toast.cancel();
                            }
                            toast = Toast.makeText(mContext, "This Product is already in card.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
            });

        } else {
            layoutActionBuy.setVisibility(View.GONE);
            layoutOutOFStock.setVisibility(View.VISIBLE);
        }
    }

    public void getParentType(){
        client.get(BackendServer.url+"/api/ecommerce/listingLite/"+itemGY+"/", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                    try {
                        ListingLite lite = new ListingLite(response);
                        listingLites.add(lite);
                        String data = response.getString("specifications");
                        JSONArray dataJson = new JSONArray(data);
                        for (int j = 0; j < dataJson.length(); j++) {
                            JSONObject object = dataJson.getJSONObject(j);
                            name = object.getString("name");
                            value = object.getString("value");
                            fieldType = object.getString("fieldType");
                            helpText = object.getString("helpText");
                            unit = object.getString("unit");
                            jsonData = object.getString("data");
                            jsonArray.put(object);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                if (listingLites.size()<=0){
                    return;
                }
                itemDetails.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                lite = listingLites.get(0);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
//                String name = getIntent().getExtras().getString(lite.getParentTypeName().toUpperCase());
                getSupportActionBar().setTitle(lite.getParentTypeName().toUpperCase());
                final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_items_detail);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        ItemDetailsActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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

                init();
                getSuggestedItem();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void getSuggestedItem(){
        client.get(BackendServer.url+"/api/ecommerce/listingLite/?parentValue="+lite.getParentTypeGY()+"&detailValue="+lite.getGY(),
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        for (int i=0; i<response.length(); i++){
                            try {
                                JSONObject object = response.getJSONObject(i);
                                ListingLite parent = new ListingLite(object);
                                suggestList.add(parent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        suggestedRecyclerView.setLayoutManager(layoutManager);
                        SuggestedRecyclerViewAdapter viewAdapter = new SuggestedRecyclerViewAdapter(suggestList);
                        suggestedRecyclerView.setAdapter(viewAdapter);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item_details, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_cart);
        NotificationCountSetClass.setAddToCart(ItemDetailsActivity.this, item, MainActivity.notificationCountCart);
        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            return true;
        } else if (id == R.id.action_cart) {

           /* NotificationCountSetClass.setAddToCart(MainActivity.this, item, notificationCount);
            invalidateOptionsMenu();*/
            startActivity(new Intent(ItemDetailsActivity.this, CartListActivity.class));

           /* notificationCount=0;//clear notification count
            invalidateOptionsMenu();*/
            return true;
        }else if (id == R.id.action_wishlist) {
            lite = listingLites.get(0);
            item.setIcon(R.drawable.ic_favorite_red_24dp);
            RequestParams params = new RequestParams();
            params.put("product", lite.getGY());
            params.put("qty", "1");
            params.put("type", "favourite");
            params.put("user", MainActivity.userGY);
            client.post(BackendServer.url + "/api/ecommerce/cart/", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (toast!= null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(mContext, "Item added to wishlist.", Toast.LENGTH_SHORT);
                    toast.show();

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(mContext, "This Product is already in wishlist.", Toast.LENGTH_SHORT).show();
                    if (toast!= null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(mContext, "This Product is already in wishlist.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    protected class SpecificationsListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return jsonArray == null ? 0 : jsonArray.length();
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
            TextView propertyName, propertyValue;
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.layout_specifications_style, viewGroup, false);
            propertyName = v.findViewById(R.id.property_name);
            propertyValue = v.findViewById(R.id.property_value);
            try {
                JSONObject obj = jsonArray.getJSONObject(position);
                propertyName.setText(obj.getString("name"));
                propertyValue.setText(obj.getString("value"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return v;
        }
    }

    private class SuggestedRecyclerViewAdapter extends RecyclerView.Adapter<MyHolder>{
        ArrayList<ListingLite> suggestedList;
        BackendServer backendServer = new BackendServer(mContext);
        AsyncHttpClient client = backendServer.getHTTPClient();
        Toast toast;
        String sku;

        public SuggestedRecyclerViewAdapter(ArrayList<ListingLite> suggestLists){
            this.suggestedList = suggestLists;
        }


        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            final ListingLite parent = suggestedList.get(position);
            String link;
            if (parent.isInStock()) {
                holder.itemsOut.setVisibility(View.GONE);
                String qunt = parent.getAddedCart();
                int qntAdd = Integer.parseInt(qunt);
                if (qntAdd == 0) {
                    holder.mCart2.setVisibility(View.VISIBLE);
                    holder.itemsQuantity.setVisibility(View.GONE);

                    holder.mCartBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (MainActivity.username.equals("")) {
                                mContext.startActivity(new Intent(mContext, LoginPageActivity.class));
                            } else {
                                RequestParams params = new RequestParams();
                                params.put("prodSku", sku);
                                params.put("product", parent.getGY());
                                params.put("qty", "1");
                                params.put("typ", "cart");
                                params.put("user", MainActivity.userGY);
                                client.post(BackendServer.url + "/api/ecommerce/cart/", params, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        holder.mCart2.setVisibility(View.GONE);
                                        holder.itemsQuantity.setVisibility(View.VISIBLE);
                                        holder.mWishlist.setImageResource(R.drawable.ic_favorite_border_green_24dp);
                                        holder.res = true;
                                        if (toast != null) {
                                            toast.cancel();
                                        }
                                        toast = Toast.makeText(mContext, "Item added to cart.", Toast.LENGTH_SHORT);
                                        toast.show();
                                        MainActivity.notificationCountCart++;
                                        NotificationCountSetClass.setNotifyCount(MainActivity.notificationCountCart);
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        if (toast != null) {
                                            toast.cancel();
                                        }
                                        toast = Toast.makeText(mContext, "This Product is already in card.", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });
                            }
                        }
                    });
                } else {
                    holder.itemsQuantity.setVisibility(View.VISIBLE);
                    holder.mCart2.setVisibility(View.GONE);
                }
            } else {
                holder.itemsOut.setVisibility(View.VISIBLE);
                holder.itemsQuantity.setVisibility(View.GONE);
                holder.mCart2.setVisibility(View.GONE);
            }

            if (parent.getFilesAttachment().equals("null")){
                link = BackendServer.url+"/static/images/ecommerce.jpg";
            } else
                link = parent.getFilesAttachment();
            Glide.with(mContext)
                    .load(link)
                    .into(holder.mImageView);

            Double d = Double.parseDouble(parent.getProductPrice());
            final int price = (int) Math.round(d);
            Double d1 = Double.parseDouble(parent.getProductDiscountedPrice());
            final int price1 = (int) Math.round(d1);

            holder.itemName.setText(parent.getProductName());

            String spinnerstr = parent.getHowMuch()+" "+parent.getUnit();

            if (parent.getProductDiscount().equals("0")){
                holder.itemPrice.setText("\u0024"+ price);
                spinnerstr += " - \u0024"+ price;
                holder.itemDiscountPrice.setVisibility(View.GONE);
                holder.itemDiscount.setVisibility(View.GONE);
            } else {
                holder.itemPrice.setText("\u0024"+price1);
                holder.itemDiscountPrice.setVisibility(View.VISIBLE);
                holder.itemDiscountPrice.setText("\u0024"+price);
                spinnerstr += " - \u0024"+ price1;
                holder.itemDiscountPrice.setPaintFlags(holder.itemDiscountPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                holder.itemDiscount.setVisibility(View.VISIBLE);
                holder.itemDiscount.setText(parent.getProductDiscount()+"% OFF");
            }
            HashMap map = new HashMap();
            map.put(holder.keys[0], spinnerstr);
            map.put("sku", parent.getSerialNo());
            map.put("disPer", parent.getProductDiscount());
            map.put("discount", price1);
            holder.spinnerlist.add(map);
            JSONArray array = parent.getItemArray();
            if (array.length()>0) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = array.getJSONObject(i);
                        String sku = jsonObj.getString("sku");
                        String updated = jsonObj.getString("updated");
                        String unitPerpack = jsonObj.getString("unitPerpack");
                        String created = jsonObj.getString("created");
                        String pricearray = jsonObj.getString("price");
                        String discountedPrice = jsonObj.getString("discountedPrice");
                        String parent_id = jsonObj.getString("parent_id");
                        String id = jsonObj.getString("id");
                        Double rspoint = Double.parseDouble(pricearray);
                        final int rs = (int) Math.round(rspoint);
                        Double rsPointdis = Double.parseDouble(discountedPrice);
                        final int rsdis = (int) Math.round(rsPointdis);
                        String  strvalue = (Double.parseDouble(parent.getHowMuch())*Integer.parseInt(unitPerpack))+" "+ parent.getUnit()+" - \u20B9"+ rs;
                        HashMap map1 = new HashMap();
                        map1.put(holder.keys[0], strvalue);
                        map1.put("sku", sku);
                        map1.put("disPer", parent.getProductDiscount());
                        map1.put("discount", rsdis);
                        holder.spinnerlist.add(map1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MainActivity.username.equals("")) {
                        mContext.startActivity(new Intent(mContext, LoginPageActivity.class));
                    } else {
                        Intent intent = new Intent(mContext, ItemDetailsActivity.class);
//                        intent.putExtra(STRING_IMAGE_URI, parent.getFilesAttachment());
//                        intent.putExtra(STRING_IMAGE_POSITION, position);
                        intent.putExtra("listingLiteGY", parent.getGY());
                        mContext.startActivity(intent);
                    }
                }
            });

            SimpleAdapter adapter = new SimpleAdapter(mContext, holder.spinnerlist, android.R.layout.simple_spinner_dropdown_item, holder.keys,holder.ids);
            holder.mItem.setAdapter(adapter);

            holder.mItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    HashMap map = (HashMap) holder.spinnerlist.get(position);
                    String spinnerValue = (String) map.get(holder.keys[0]);
                    sku = (String) map.get("sku");
                    String disPer = (String) map.get("disPer");
                    int discount = (int) map.get("discount");
                    Log.e("onItemClick",(String) map.get("sku")+" "+spinnerValue);
                    String arrSplit[] = spinnerValue.split("-");
                    if (disPer.equals("0")){
                        holder.itemPrice.setText(arrSplit[1]);
                        holder.itemDiscountPrice.setVisibility(View.GONE);
                        holder.itemDiscount.setVisibility(View.GONE);
                    } else {
                        holder.itemPrice.setText("\u0024"+discount);
                        holder.itemDiscountPrice.setVisibility(View.VISIBLE);
                        holder.itemDiscountPrice.setText(""+arrSplit[1]);
                        holder.itemDiscountPrice.setPaintFlags(holder.itemDiscountPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                        holder.itemDiscount.setVisibility(View.VISIBLE);
                        holder.itemDiscount.setText(disPer+"% OFF");
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //Set click action for wishlist
            String quntWish = parent.getAddedWish();
            int qntWishAdd = Integer.parseInt(quntWish);
            if (qntWishAdd==0) {
                holder.mWishlist.setImageResource(R.drawable.ic_favorite_border_green_24dp);
                holder.res = true;
            }else {
                holder.mWishlist.setImageResource(R.drawable.ic_favorite_red_24dp);
                holder.res = false;
            }

            holder.mWishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MainActivity.username.equals("")) {
                        mContext.startActivity(new Intent(mContext, LoginPageActivity.class));
                    } else {
                        if (holder.res) {
                            RequestParams params = new RequestParams();
                            params.put("prodSku", sku);
                            params.put("product", parent.getGY());
                            params.put("qty", 1);
                            params.put("typ", "favourite");
                            params.put("user", MainActivity.userGY);
                            client.post(BackendServer.url + "/api/ecommerce/cart/", params, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    holder.mWishlist.setImageResource(R.drawable.ic_favorite_red_24dp);
                                    holder.itemsQuantity.setVisibility(View.GONE);
                                    holder.mCart2.setVisibility(View.VISIBLE);
                                    MainActivity.notificationCountCart--;
                                    NotificationCountSetClass.setNotifyCount(MainActivity.notificationCountCart);
                                    holder.res = false;
                                    if (toast != null) {
                                        toast.cancel();
                                    }
                                    toast = Toast.makeText(mContext, "Item added to wishlist.", Toast.LENGTH_SHORT);
                                    toast.show();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    Toast toast = null;
                                    if (toast != null) {
                                        toast.cancel();
                                    }
                                    toast = Toast.makeText(mContext, "This Product is already in wishlist.", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                        } else {
                            RequestParams params = new RequestParams();
                            params.put("product", parent.getGY());
                            params.put("qty", 0);
                            params.put("typ", "favourite");
                            params.put("user", MainActivity.userGY);
                            client.post(BackendServer.url + "/api/ecommerce/cart/", params, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    holder.mWishlist.setImageResource(R.drawable.ic_favorite_border_green_24dp);
                                    holder.mCart2.setVisibility(View.VISIBLE);
                                    holder.itemsQuantity.setVisibility(View.GONE);
                                    holder.res = true;
                                    if (toast != null) {
                                        toast.cancel();
                                    }
                                    toast = Toast.makeText(mContext, "Item removed from wishlist.", Toast.LENGTH_SHORT);
                                    toast.show();

                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    if (toast != null) {
                                        toast.cancel();
                                    }
                                    toast = Toast.makeText(mContext, "Removing failure", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return suggestedList.size()>4 ? 4: suggestedList.size();
        }
    }

    private class MyHolder extends RecyclerView.ViewHolder{
        public final Spinner mItem;
        public final ImageView mImageView;
        public final LinearLayout mLayoutItem, mCart2;
        public final ImageView mWishlist, mCartBtn;
        TextView itemName, itemPrice, itemDiscount, itemDiscountPrice, itemsQuantity, itemsOut;
        boolean res = true;
        ArrayList spinnerlist = new ArrayList();
        String keys[] = {"str"};
        int ids[] = {android.R.id.text1};

        public MyHolder(View view) {
            super(view);
            spinnerlist.clear();
            mImageView =  view.findViewById(R.id.image1);
            mLayoutItem = view.findViewById(R.id.layout_item);
            mCart2 = view.findViewById(R.id.layout_action2_cart);
            mWishlist = view.findViewById(R.id.ic_wishlist);
            itemName =  view.findViewById(R.id.item_name);
            mItem =  view.findViewById(R.id.item_variants_spinner);
            itemPrice =  view.findViewById(R.id.item_price);
            itemDiscountPrice =  view.findViewById(R.id.actual_price);
            itemDiscount =  view.findViewById(R.id.discount_percentage);
            itemsQuantity =  view.findViewById(R.id.item_added);
            itemsOut =  view.findViewById(R.id.out_of_stock);
            mCartBtn =  view.findViewById(R.id.card_item_quantity_add);
        }
    }
}
