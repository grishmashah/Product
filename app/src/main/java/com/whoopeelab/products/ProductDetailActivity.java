package com.whoopeelab.products;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.whoopeelab.products.product.Product;
import com.whoopeelab.products.product.ProductSqLiteHelper;


public class ProductDetailActivity extends Activity {

    ProductSqLiteHelper productSqLiteHelper;
    EditText txtProductName;
    EditText txtProductDesc;
    EditText txtProductPrice;
    EditText txtProductSalePrice;
    ImageView imgProduct;
    Boolean isUpdate;
    String product_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productSqLiteHelper = ProductSqLiteHelper.getInstance(this);

        txtProductName = (EditText)findViewById(R.id.edit_name);
        txtProductDesc= (EditText)findViewById(R.id.edit_desc);
        txtProductPrice= (EditText)findViewById(R.id.edit_price);
        txtProductSalePrice= (EditText)findViewById(R.id.edit_sale_price);
        imgProduct= (ImageView)findViewById(R.id.img_prod_detail);

        Intent intent = getIntent();
        product_id = intent.getStringExtra("product_id");
        isUpdate = intent.getBooleanExtra("update_product",false);

        if(!isUpdate){
            txtProductName.setFocusable(false);
            txtProductName.setBackground(null);
            txtProductDesc.setFocusable(false);
            txtProductDesc.setBackground(null);
            txtProductPrice.setFocusable(false);
            txtProductPrice.setBackground(null);
            txtProductSalePrice.setFocusable(false);
            txtProductSalePrice.setBackground(null);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        new GetProductDetailTask().execute("detail");
    }

    private class GetProductDetailTask extends AsyncTask<Object , Object, Product> {

        @Override
        protected Product doInBackground(Object... objects) {
            String action = objects[0].toString();
            if(action.equals("detail")) {
                Product product = productSqLiteHelper.getProduct(product_id);
                return product;
            }else if(action.equals("update")){
                Product product = (Product) objects[1];
                productSqLiteHelper.updateProduct(product);
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Product product) {
            if(product!=null){
                txtProductName.setText(product.getName());
                txtProductDesc.setText(product.getDescription());
                txtProductPrice.setText(String.valueOf(product.getPrice()));
                txtProductSalePrice.setText(String.valueOf(product.getSalePrice()));
                final Uri uri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() +"/raw/"+ product.getImageUri());
                imgProduct.setImageURI(uri);
                imgProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), FullscreenImageActivity.class);
                        intent.putExtra("image_uri", uri.toString());
                        startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.product_detail, menu);

        MenuItem item = menu.findItem(R.id.save_product);
        if(isUpdate){
            item.setVisible(true);
        }else{
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        boolean hasError = false;
        String message = null;
        int id = item.getItemId();
        if (id == R.id.save_product) {
            Product product = new Product();
            product.setId(Double.parseDouble(product_id));
            if(txtProductName.getText().toString() !=null && !txtProductName.getText().toString().isEmpty()) {
                product.setName(txtProductName.getText().toString());
            }else{
                hasError = true;
                message = "Please provide correct name";
            }
            if(txtProductDesc.getText().toString() !=null && !txtProductDesc.getText().toString().isEmpty()) {
                product.setDescription(txtProductDesc.getText().toString());
            }else{
                hasError = true;
                message = "Please provide correct description";
            }
            try {
                Double price = Double.parseDouble(txtProductPrice.getText().toString());
                product.setPrice(price);
                Double salePrice = Double.parseDouble(txtProductSalePrice.getText().toString());
                product.setSalePrice(salePrice);
            }catch (NumberFormatException e){
                hasError = true;
                message = "Please provide valid value";
            }

            if(hasError){
                Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
                toast.show();
            }else {
                new GetProductDetailTask().execute("update", product);

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }

            return true;
        }else if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
