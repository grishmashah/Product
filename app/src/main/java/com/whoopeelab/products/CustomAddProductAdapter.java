package com.whoopeelab.products;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.whoopeelab.products.product.Product;

import java.util.List;

/**
 * Created by grishmashah on 9/19/14.
 */
public class CustomAddProductAdapter extends ArrayAdapter {

    Context context;

    public CustomAddProductAdapter(Context context, int resource, List<Product> items) {
        super(context, resource, items);
        this.context = context;
    }

    private class AddProductViewHolder{
        CheckBox checkedProduct;
        TextView txtProduct;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        AddProductViewHolder addProductViewHolder =null;

        Product rowItem = (Product) getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.view_add_product, null);
            addProductViewHolder = new AddProductViewHolder();
            addProductViewHolder.checkedProduct = (CheckBox) convertView.findViewById(R.id.check_product);
            addProductViewHolder.txtProduct = (TextView) convertView.findViewById(R.id.txt_product_name);

            addProductViewHolder.checkedProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox cb = (CheckBox) view;
                    Product product = (Product) cb.getTag();
                    product.setChecked(cb.isChecked());
                    Log.v(getClass().getName(), "Coming here in onClick od checkbox");
                }
            });

            convertView.setTag(addProductViewHolder);
        }else{
            addProductViewHolder = (AddProductViewHolder) convertView.getTag();
        }

        addProductViewHolder.txtProduct.setText(rowItem.getName());
        addProductViewHolder.checkedProduct.setChecked(rowItem.isChecked());
        addProductViewHolder.checkedProduct.setTag(rowItem);

        return convertView;
    }
}
