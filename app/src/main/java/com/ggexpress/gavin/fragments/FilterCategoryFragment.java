package com.ggexpress.gavin.fragments;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.ggexpress.gavin.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FilterCategoryFragment extends Fragment {

    String categoryItems[] = {"Clothing","Gift cards","Home & Kitchen","Video Games","Groceries","Toys","Electronics","Appliances","Under $1000","Automotive Parts & Accessories","Baby","Books","Gardon & Outdoor","Tools & Home Improvement","Subscribe & Save","Pets Supply","Beauty and Personal Care","Pharmacy","Art, Crafts & Sewing","Cell Phones & Accessories"};
    String brandItems[] = {"Adidas Originals","Being Human","Breakbounce","Baby Formula","Flyrs Club","HIGHLANDER","Indigo Nation","Apple","Samsung","Dior","Chanel","Calvin Klein","Prada","Lacoste","Celine","Tommy Hilgiger","Vans","Rolex","Louis Vuitton","Nike","Burberry","Fenty Beauty","Dolce & Gabbana","Playstation","Sony","Nintendo","Xbox","Acer","Lg","Pampers","Huggies","Gucci","Gucci","Gucci","Gucci",};
    String filterByItems[] = {"Adidas Originals","Being Human","Breakbounce","Duke","Flyrs Club","HIGHLANDER","Indigo Nation"};
    String sizeItems[] = {"34","36","38","40","42","44","46"};
    String typeItems[] = {"Abstract","Buffalo Checks","Camouflage","Geometric","Micro Ditsy","Solid","Textured"};
    String getArgument = FilterItemsActivity.filterName;
    String filteStr;

    public FilterCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter_category, container, false);
        ListView filterList = view.findViewById(R.id.filter_category_list);
//        if (filteStr!=null) {
//            filteStr = getArguments().getString("filter");
//            Toast.makeText(getContext(), ""+filteStr, Toast.LENGTH_SHORT).show();
//        }
        FilterListAdapter listAdapter = new FilterListAdapter(getContext());
        filterList.setAdapter(listAdapter);
        return view;
    }


    private class FilterListAdapter extends BaseAdapter {
        Context context;
        public FilterListAdapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return categoryItems.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView categoryName;
            CheckBox categoryCheckBox;

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_filter_list, parent, false);
            categoryName = v.findViewById(R.id.category_name);
            categoryCheckBox = v.findViewById(R.id.category_cb);

            if (getArgument.equals("category")) {
                categoryName.setText(categoryItems[position]);
            }
            if (getArgument.equals("brand")) {
                categoryName.setText(brandItems[position]);
            }
            if (getArgument.equals("filterBy")) {
                categoryName.setText(categoryItems[position]);
            }
            if (getArgument.equals("size")) {
                categoryName.setText(sizeItems[position]);
            }
            if (getArgument.equals("type")) {
                categoryName.setText(typeItems[position]);
            } else if (getArgument.equals("")) {
                categoryName.setText(categoryItems[position]);
            }

            return v;
        }
    }
}
