package com.ggexpress.gavin.options;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ggexpress.gavin.backend.BackendServer;
import com.ggexpress.gavin.R;
import com.ggexpress.gavin.entites.Address;
import com.ggexpress.gavin.payment.PaymentActivity;
import com.ggexpress.gavin.startup.MainActivity;
import com.githang.stepview.StepView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CheckOutActivity extends AppCompatActivity {
    private static Context mContext;
    TextView textAmount, textAddNewAddress;
    Button newAddressBtn;
    RecyclerView recyclerView;
    AsyncHttpClient client;
    public static ArrayList<Address> addresses;
    public static int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        mContext = CheckOutActivity.this;
        BackendServer backend = new BackendServer(this);
        client = backend.getHTTPClient();
        addresses = new ArrayList<>();
        getitem();
        StepView mStepView = (StepView) findViewById(R.id.step_view);
        List<String> steps = Arrays.asList(new String[]{"Selected Items", "Shipping Address", "Review Your Order"});
        mStepView.setSteps(steps);
        mStepView.selectedStep(2);
        init();
    }

    public void init(){
        newAddressBtn = findViewById(R.id.text_action_continue);
        textAmount = findViewById(R.id.text_action_amount);
        textAddNewAddress = findViewById(R.id.no_address);
        recyclerView = findViewById(R.id.recyclerview_address);
    }

    public boolean checkAddress() {
        if(addresses.size()<=0) {
            recyclerView.setVisibility(View.GONE);
            textAddNewAddress.setVisibility(View.VISIBLE);
            return false;
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            textAddNewAddress.setVisibility(View.GONE);
            return true;
        }
    }
    public void getitem() {
        client.get(BackendServer.url+"/api/ecommerce/address/?user="+ MainActivity.userGY, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                for (int i = 0; i < response.length(); i++) {
                    JSONObject object = null;
                    try {
                        object = response.getJSONObject(i);
                        Address address = new Address(object);
                        addresses.add(address);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                textAmount.setText("\u0024" + getIntent().getExtras().getInt("totalPrice"));
                newAddressBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext, NewAddressActivity.class)
                                .putExtra("totalPrice", textAmount.getText().toString()));
                    }
                });
                if (checkAddress()) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                    recyclerView.setAdapter(new CheckOutActivity.AddressRecyclerViewAdapter(addresses));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }


    public class AddressRecyclerViewAdapter extends RecyclerView.Adapter<CheckOutActivity.AddressRecyclerViewAdapter.ViewHolder> {
        private ArrayList<Address> mAddresslist;
        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            TextView addresstxt;
            LinearLayout deliveryAction;
            ImageView delAdd;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                addresstxt =  view.findViewById(R.id.address_text);
                deliveryAction =  view.findViewById(R.id.delivery_action);
                delAdd =  itemView.findViewById(R.id.delete_address);
                delAdd.setVisibility(View.GONE);
            }
        }

        public AddressRecyclerViewAdapter(ArrayList<Address> addresses) {
            mAddresslist = addresses;
        }

        @NonNull
        @Override
        public CheckOutActivity.AddressRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_address_list, parent, false);
            return new CheckOutActivity.AddressRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder,int position) {

            final Address address = mAddresslist.get(position);
            holder.addresstxt.setText(address.getTitle()+"\n"+address.getStreet()+"\n"+address.getLandMark()+"\n"
                    +address.getCity()+", "+address.getRegion()+" "+address.getBuyerName()+"\n"+address.getCountry());
            holder.deliveryAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos=position;
                    startActivity(new Intent(mContext, PaymentActivity.class)
                            .putExtra("address", holder.addresstxt.getText().toString())
                            .putExtra("pk", address.getGY())
                            .putExtra("totalPrice", textAmount.getText().toString()));
                }
            });
        }

        @Override
        public void onViewRecycled(CheckOutActivity.AddressRecyclerViewAdapter.ViewHolder holder) {
        }

        @Override
        public int getItemCount() {
            return mAddresslist.size();
        }
    }
}
