package com.whoopeelab.products;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by grishmashah on 9/20/14.
 */
public class CustomListProductAdapter extends ResourceCursorAdapter {

    private LayoutInflater mInflater;

    CustomListProductAdapter(Context context, int layout, Cursor c, int flags){
        super(context, layout, c, flags);

        mInflater = LayoutInflater.from(context);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View retView = mInflater.inflate(R.layout.view_list_product, null);
        ProductViewHolder viewHolder = new ProductViewHolder();
        viewHolder.productImg = (ImageView) retView.findViewById(R.id.img_prod);
        viewHolder.txtProduct = (TextView) retView.findViewById(R.id.txt_prod);
        retView.setTag(viewHolder);

        return retView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ProductViewHolder viewHolder = (ProductViewHolder) view.getTag();
        String product_name = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1)));
        viewHolder.txtProduct.setText(product_name);
        Uri image_uri = Uri.parse("android.resource://" + context.getPackageName() +"/raw/"+
                cursor.getString(cursor.getColumnIndex(cursor.getColumnName(5))));
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), image_uri);
            viewHolder.productImg.setImageBitmap(scaleBitmap(bitmap));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ProductViewHolder{
        ImageView productImg;
        TextView txtProduct;
    }


        private static Bitmap scaleBitmap(Bitmap source) {
            int maxSize = source.getWidth() > source.getHeight() ? source.getWidth() : source.getHeight();
            return Bitmap.createScaledBitmap(source, source.getWidth() * 96 / maxSize, source.getHeight() * 96 / maxSize, true);
        }
    }

