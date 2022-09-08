package com.ggexpress.gavin.options;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ggexpress.gavin.backend.BackendServer;
import com.ggexpress.gavin.R;
import com.ggexpress.gavin.entites.Cart;
import com.ggexpress.gavin.entites.ListingParent;
import com.ggexpress.gavin.product.ItemDetailsActivity;
import com.ggexpress.gavin.startup.MainActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class WishlistActivity extends AppCompatActivity {
    private static Context mContext;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    Button bStartShopping;
    TextView noItem;
    public static LinearLayout layoutCartItems, layoutCartNoItems;
    public AsyncHttpClient client;
    public static ArrayList<Cart> wishList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        mContext = WishlistActivity.this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BackendServer backend = new BackendServer(mContext);
        client = backend.getHTTPClient();

        wishList = new ArrayList<>();
        getListItem();
        recyclerView = findViewById(R.id.recyclerview);
        progressBar =  findViewById(R.id.progressBar);

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
    protected void setLayout(){
        layoutCartItems = findViewById(R.id.layout_items);
        layoutCartNoItems = findViewById(R.id.layout_cart_empty);
        bStartShopping =  findViewById(R.id.bAddNew);
        noItem =  findViewById(R.id.tvInfo);

        if (wishList.size() >0) {
            layoutCartNoItems.setVisibility(View.GONE);
            layoutCartItems.setVisibility(View.VISIBLE);
//            layoutCartPayments.setVisibility(View.VISIBLE);

        } else {
            layoutCartNoItems.setVisibility(View.VISIBLE);
            layoutCartItems.setVisibility(View.GONE);
            noItem.setText("No items yet");

            bStartShopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    public void getListItem() {
        client.get(BackendServer.url+"/api/ecommerce/cart/?&Name__contains=&typ=favourite&user="+MainActivity.userGY,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                Cart cart = new Cart(object);
                                wishList.add(cart);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        setLayout();
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(wishList));

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.e("Error "+statusCode, "onFailure2");
                    }
        });
    }



    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<WishlistActivity.SimpleStringRecyclerViewAdapter.ViewHolder> {

        private ArrayList<Cart> mWishlist;
        BackendServer backendServer = new BackendServer(mContext);
        AsyncHttpClient client = backendServer.getHTTPClient();

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final SimpleDraweeView mImageView;
            public final LinearLayout mLayoutItem, mLayoutRemove;
            TextView productName, itemPrice, actualPrice, discountPercentage;
            Button move;
//            AsyncHttpClient client = new AsyncHttpClient();

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = view.findViewById(R.id.image_wishlist);
                mLayoutItem = view.findViewById(R.id.layout_item_desc);
                mLayoutRemove = view.findViewById(R.id.layout_action1_remove);
//                mImageViewWishlist = (ImageView) view.findViewById(R.id.ic_wishlist);
                productName =  view.findViewById(R.id.product_name);
                itemPrice =  view.findViewById(R.id.item_price);
                actualPrice =  view.findViewById(R.id.actual_price);
                actualPrice.setPaintFlags(actualPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                discountPercentage =  view.findViewById(R.id.discount_percentage);
                move =  view.findViewById(R.id.move_cart_button);
            }
        }

        public SimpleStringRecyclerViewAdapter(ArrayList<Cart> wishlist) {
            mWishlist = wishlist;
        }

        @Override
        public WishlistActivity.SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wishlist_item, parent, false);
            client = new AsyncHttpClient();
            return new WishlistActivity.SimpleStringRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            if (holder.mImageView.getController() != null) {
                holder.mImageView.getController().onDetach();
            }
            if (holder.mImageView.getTopLevelDrawable() != null) {
                holder.mImageView.getTopLevelDrawable().setCallback(null);
//                ((BitmapDrawable) holder.mImageView.getTopLevelDrawable()).getBitmap().recycle();
            }
        }

        @Override
        public void onBindViewHolder(final WishlistActivity.SimpleStringRecyclerViewAdapter.ViewHolder holder, final int position) {
            final Cart cart = mWishlist.get(position);
            final ListingParent parent = cart.getParents().get(0);
            final Uri uri = Uri.parse(cart.getListingParent().getFilesAttachment());
            holder.mImageView.setImageURI(uri);

            holder.productName.setText(parent.getProductName());
            if (parent.getProductDiscount().equals("0")){
                holder.itemPrice.setText("\u0024"+parent.getProductPrice());
                holder.actualPrice.setVisibility(View.GONE);
                holder.discountPercentage.setVisibility(View.GONE);
//                mPrice = mPrice + (parent.getProductIntPrice()*Integer.parseInt(cart.getQuantity()));
            } else {
                holder.itemPrice.setText("\u0024"+parent.getProductDiscountedPrice());
//                mPrice = mPrice + (parent.getProductIntDiscountedPrice()*Integer.parseInt(cart.getQuantity()));
                holder.discountPercentage.setVisibility(View.VISIBLE);
                holder.discountPercentage.setText("("+parent.getProductDiscount()+"% OFF)");
                holder.actualPrice.setVisibility(View.VISIBLE);
                holder.actualPrice.setText("\u0024"+parent.getProductPrice());
                holder.actualPrice.setPaintFlags(holder.actualPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            }


            holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ItemDetailsActivity.class);
//                    intent.putExtra(STRING_IMAGE_URI, cart.getListingParent().getFilesAttachment());
//                    intent.putExtra(STRING_IMAGE_POSITION, position);
                    intent.putExtra("listingLiteGY", parent.getGY());
                    mContext.startActivity(intent);
                }
            });

            holder.mLayoutRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem(cart, position);
                    notifyDataSetChanged();
                }
            });

            //Set click action for wishlist
            holder.move.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moveCart(cart);
                    MainActivity.notificationCountCart++;

//                    ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
//                    imageUrlUtils.removeWishlistImageUri(position);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mWishlist.size();
        }

        public void moveCart(Cart cart) {
            RequestParams params = new RequestParams();
            params.put("typ", "cart");
            client.patch(BackendServer.url+"/api/ecommerce/cart/"+ cart.getGY()+"/", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Toast.makeText(mContext, "onSuccess", Toast.LENGTH_SHORT).show();
                    mContext.startActivity(new Intent(mContext, WishlistActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(mContext, "onFailure", Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void deleteItem(final Cart cart, final int position) {
            client.delete(mContext, BackendServer.url + "/api/ecommerce/cart/"+ cart.getGY()+"/", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Toast.makeText(mContext, "removed"+ cart.getGY(), Toast.LENGTH_SHORT).show();
                    mContext.startActivity(new Intent(mContext, WishlistActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(mContext, "removing failure"+ cart.getGY(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
