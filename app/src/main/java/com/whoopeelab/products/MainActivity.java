package com.whoopeelab.products;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.whoopeelab.products.product.Product;
import com.whoopeelab.products.product.ProductSqLiteHelper;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity{

    ProductSqLiteHelper productSqLiteHelper;
    private List<Product> productList = new ArrayList<Product>();
    ListView productListView;
    CustomListProductAdapter mainListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);

        productListView = (ListView) findViewById(R.id.list_products);

        productSqLiteHelper = ProductSqLiteHelper.getInstance(this);

        mainListAdapter = new CustomListProductAdapter(this,
                R.layout.view_list_product,
                null,
                0);
        productListView.setAdapter(mainListAdapter);
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), ProductDetailActivity.class);
                intent.putExtra("product_id", String.valueOf(l));
                startActivity(intent);
            }
        });

        registerForContextMenu(productListView);
    }


    @Override
    public void onResume(){
        super.onResume();
        new ListProductsTask().execute("list");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.add_product){
            Intent intent = new Intent(this, AddProductActivty.class);
            startActivity(intent);
            return true;

        }else if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.update_product:
                updateProduct(info.id);
                return true;
            case R.id.delete_product:
                deleteProduct(info.id);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void deleteProduct(long id){
        //productSqLiteHelper.deleteProduct(id);
        new ListProductsTask().execute("delete",id);
    }

    public void updateProduct(long id){
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product_id", String.valueOf(id));
        intent.putExtra("update_product", true);
        startActivity(intent);
    }

    private class ListProductsTask extends AsyncTask<Object , Object, Cursor>{

        @Override
        protected Cursor doInBackground(Object... objects) {
            String action = objects[0].toString();
            if(action.equals("list")) {
                return productSqLiteHelper.getAllProductsWithCursor();
            }else if(action.equals("delete")){
                long id = Long.parseLong(objects[1].toString());
                productSqLiteHelper.deleteProduct(id);
                return productSqLiteHelper.getAllProductsWithCursor();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Cursor result) {
            mainListAdapter.changeCursor(result);
        }
    }
}
