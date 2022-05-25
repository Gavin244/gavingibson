package com.ggexpress.gavin.startup;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.appbar.AppBarLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.GravityCompat;
import android.view.View;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.ggexpress.gavin.backend.BackendServer;
import com.ggexpress.gavin.R;
import com.ggexpress.gavin.entites.Cart;
import com.ggexpress.gavin.entites.Generic;
import com.ggexpress.gavin.entites.ListingParent;
import com.ggexpress.gavin.entites.Offer;
import com.ggexpress.gavin.fragments.ImageListFragment;
import com.ggexpress.gavin.miscellaneous.EmptyActivity;
import com.ggexpress.gavin.notification.NotificationCountSetClass;
import com.ggexpress.gavin.options.CartListActivity;
import com.ggexpress.gavin.options.FeedBackActivity;
import com.ggexpress.gavin.options.HelpCenterActivity;
import com.ggexpress.gavin.options.MyAccountActivity;
import com.ggexpress.gavin.options.OrderActivity;
import com.ggexpress.gavin.options.SearchResultActivity;
import com.ggexpress.gavin.options.WishlistActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static int notificationCountCart = 0;
    public static String username = "";
    public static String userPK;
    static ViewPager viewPager;
    static TabLayout tabLayout;
    BottomNavigationView navigationBottom;
    Context context;
    TextView prflName;
    LinearLayout navHeadLayout;
    SimpleDraweeView userImage;
    private SliderImageFragmentAdapter mSliderImageFragmentAdapter;
    private ViewPager mViewPager;
    private ExtensiblePageIndicator extensiblePageIndicator;
//    public static ArrayList<HashMap> hashMapList;
    public static ArrayList<ListingParent> listingParents;

    private AsyncHttpClient client;

    public static ArrayList<Generic> generics;
    public static ArrayList<Offer> offerList;
    public static ArrayList<Cart> cartList;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = MainActivity.this;

        BackendServer backend = new BackendServer(context);
        client = backend.getHTTPClient();
        generics = new ArrayList<Generic>();
        offerList = new ArrayList<Offer>();
        cartList = new ArrayList<Cart>();
        listingParents = new ArrayList<ListingParent>();
        getUserDetails();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle(null);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        toggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu_green_24dp, getApplicationContext().getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationBottom = findViewById(R.id.bottom_navigation);
        navigation();
        View v =  navigationView.getHeaderView(0);
        navHeadLayout = v.findViewById(R.id.nav_head_ll);
        prflName = v.findViewById(R.id.user_name);
        userImage = v.findViewById(R.id.user_image);
        AnimationDrawable animationDrawable = (AnimationDrawable) navHeadLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        initCollapsingToolbar();
        geProduct();
        navHeadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.equals(""))
                    startActivity(new Intent(context, LoginActivity.class));
                else
                    startActivity(new Intent(context, MyAccountActivity.class));
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (exit) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Press again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_cart);
        NotificationCountSetClass.setAddToCart(MainActivity.this, item, notificationCountCart);
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (username.equals("")) {
            startActivity(new Intent(context, LoginActivity.class));
        } else {
            if (id == R.id.action_search) {
                startActivity(new Intent(MainActivity.this, SearchResultActivity.class));
                return true;
            } else if (id == R.id.action_cart) {
                startActivity(new Intent(MainActivity.this, CartListActivity.class));
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void geProduct() {
        client.get(BackendServer.url+"/api/ecommerce/genericProduct/", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                for (int i=0; i<response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Generic product = new Generic(object);
                        generics.add(product);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

        client.get(BackendServer.url+"/api/ecommerce/offerBanner/", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                for (int i=0; i<response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Offer banners = new Offer(object);
                        offerList.add(banners);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

        client.get(BackendServer.url+"/api/ecommerce/cart/?&typ=cart&user="+MainActivity.userPK,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        for (int i=0; i<response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                Cart cart = new Cart(object);
                                cartList.add(cart);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        notificationCountCart = cartList.size();
                        if (viewPager != null) {
                            getItems();
                            getViewpagerFragment();
                            setupViewPager(viewPager);
                            tabLayout.setupWithViewPager(viewPager);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
    }

    private void setupViewPager(ViewPager viewPager) {
        ListFragmentAdapter adapter = new ListFragmentAdapter(getSupportFragmentManager());
        for (int i = 0; i< generics.size(); i++) {
            ImageListFragment fragment = new ImageListFragment();
            Bundle bundle = new Bundle();
            Generic product = generics.get(i);
            bundle.putInt("type", i+1);
            bundle.putString("pk", product.getPk());
            fragment.setArguments(bundle);
            adapter.addFragment(fragment, product.getName());

        }
        viewPager.setAdapter(adapter);
    }

    public void getItems() {
        for (int i = 0; i< generics.size(); i++) {
            Generic product = generics.get(i);
            client.get(BackendServer.url + "/api/ecommerce/listing/?parent=" + product.getPk() + "&recursive=1", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = response.getJSONObject(i);
                            ListingParent parent = new ListingParent(object);
                            listingParents.add(parent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Toast.makeText(context, "onFailure", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_item1) {
//            viewPager.setCurrentItem(0);
//        } else if (id == R.id.nav_item2) {
//            viewPager.setCurrentItem(1);
//        } else if (id == R.id.nav_item3) {
//            viewPager.setCurrentItem(2);
//        } else if (id == R.id.nav_item4) {
//            viewPager.setCurrentItem(3);
//        } else if (id == R.id.nav_item5) {
//            viewPager.setCurrentItem(4);
//        }else if (id == R.id.nav_item6) {
//            viewPager.setCurrentItem(5);
//        } else

        if (id == R.id.help_center) {
            startActivity(new Intent(MainActivity.this, HelpCenterActivity.class));
        } else if (id == R.id.contact_us) {
            startActivity(new Intent(MainActivity.this, FeedBackActivity.class));
        } else if (id == R.id.terms_conditions) {
            startActivity(new Intent(MainActivity.this, WebViewActivity.class)
                    .putExtra("term", false));
        }else {
            if (username.equals("")) {
                startActivity(new Intent(context, LoginActivity.class));
            } else {
                if (id == R.id.my_wishlist) {
                    startActivity(new Intent(MainActivity.this, WishlistActivity.class));
                } else if (id == R.id.my_cart) {
                    startActivity(new Intent(MainActivity.this, CartListActivity.class));
                } else if (id == R.id.my_account) {
                    startActivity(new Intent(MainActivity.this, MyAccountActivity.class));
                } else
                    startActivity(new Intent(MainActivity.this, EmptyActivity.class));
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void navigation() {
        navigationBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (username.equals("")) {
                    startActivity(new Intent(context, LoginPageActivity.class));
                    return true;
                } else {
                    switch (item.getItemId()) {
                        case R.id.action_cart: {
                            startActivity(new Intent(MainActivity.this, CartListActivity.class));
                            return true;
                        }
                        case R.id.action_order: {
                            startActivity(new Intent(MainActivity.this, OrderActivity.class));
                            return true;
                        }
                        case R.id.action_support: {
                            startActivity(new Intent(MainActivity.this, FeedBackActivity.class));
                            return true;
                        }
                        case R.id.action_home: {
//                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    void getUserDetails() {
        client.get(BackendServer.url + "/api/HR/users/?mode=mySelf&format=json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.e("MainActivity","onSuccess");
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONObject usrObj = response.getJSONObject(0);
                    userPK = usrObj.getString("pk");
                    username = usrObj.getString("username");
                    String firstName = usrObj.getString("first_name");
                    String lastName = usrObj.getString("last_name");
                    String email = usrObj.getString("email");
                    JSONObject profileObj = usrObj.getJSONObject("profile");

                    String dpLink = profileObj.getString("displayPicture");
                    if (dpLink.equals("null")||dpLink==null){
                        dpLink = BackendServer.url+"static/images/userIcon.png";
                    }
                    String mobile = profileObj.getString("mobile");
                    prflName.setText(firstName+" "+lastName);
                    Uri uri = Uri.parse(dpLink);
                    userImage.setImageURI(uri);
                } catch (JSONException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("MainActivity","onFailure");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Log.e("MainActivity","onFinish");
            }
        });
    }

    static class ListFragmentAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public ListFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.slider_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
//                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
//                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
    int pos;
    public void getViewpagerFragment() {
        extensiblePageIndicator = (ExtensiblePageIndicator) findViewById(R.id.flexibleIndicator);
        mSliderImageFragmentAdapter = new SliderImageFragmentAdapter(getSupportFragmentManager());
        for (int i = 0; i< offerList.size(); i++) {
            Offer banners = offerList.get(i);
            mSliderImageFragmentAdapter.addFragment(OfferBannerFragment.newInstance(android.R.color.transparent, banners.getImage()));
        }
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSliderImageFragmentAdapter);
        extensiblePageIndicator.initViewPager(mViewPager);
        final GestureDetectorCompat tapGestureDetector = new GestureDetectorCompat(this, new TapGestureListener());
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                tapGestureDetector.onTouchEvent(event);
                return false;
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                pos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    class TapGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (username.equals("")) {
                startActivity(new Intent(context, LoginPageActivity.class));
                return super.onSingleTapConfirmed(e);
            } else {
                Offer banners = offerList.get(pos);
                startActivity(new Intent(context, WebViewActivity.class)
                        .putExtra("term", true)
                        .putExtra("body", banners.getBody())
                        .putExtra("title", banners.getPageTitle()));
                return super.onSingleTapConfirmed(e);
            }
        }
    }
}
