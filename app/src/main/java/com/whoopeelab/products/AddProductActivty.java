package com.whoopeelab.products;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.whoopeelab.products.product.Product;
import com.whoopeelab.products.product.ProductSqLiteHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AddProductActivty extends Activity {

    private List<Product> productList = new ArrayList<Product>();
    private List<Product> productsToAdd = new ArrayList<Product>();
    List<Product> mainRowItems;
    ListView productListView;
    CheckBox productChecked;
    Button btnAddProduct;
    CustomAddProductAdapter mainListAdapter;
    ProductSqLiteHelper productSqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        productList = parseJson();

        productSqLiteHelper = ProductSqLiteHelper.getInstance(this);

        mainRowItems = new ArrayList<Product>();
        for (int i=0;i<productList.size();i++){
            mainRowItems.add(productList.get(i));
        }

        productListView = (ListView) findViewById(R.id.list_add_product);
        mainListAdapter = new CustomAddProductAdapter(this, R.layout.view_add_product, mainRowItems);
        productListView.setAdapter(mainListAdapter);
        productListView.setItemsCanFocus(false);

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product product = mainRowItems.get(i);
                productsToAdd.add(product);
            }
        });

        btnAddProduct = (Button) findViewById(R.id.btn_add);
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddProductTask().execute();
            }
        });
    }

    private class AddProductTask extends AsyncTask<Object , Object, Boolean>{

        @Override
        protected Boolean doInBackground(Object... objects) {
            boolean isAdded = true;
            for (int i=0;i<mainRowItems.size();i++){
                Product product = mainRowItems.get(i);
                if(product.isChecked()) {
                    isAdded = productSqLiteHelper.addProduct(product);
                    if(!isAdded){
                        isAdded = false;
                        break;
                    }
                }
            }
            return isAdded;
        }

        @Override
        protected void onPostExecute(Boolean isAdded){
            if(!isAdded){
                Toast toast= Toast.makeText(getApplicationContext(), "Already Exists In Database", Toast.LENGTH_SHORT);
                toast.show();
            }else{
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_product_activty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public List parseJson(){
        List result = new ArrayList();

        try {
            JSONObject json = new JSONObject(mockData);
            JSONArray jsonArray = json.getJSONArray("products");
            for (int i=0; i< jsonArray.length(); i++){
                Product product = new Product();
                JSONObject json_obj = jsonArray.getJSONObject(i);
                product.setId(Double.parseDouble(json_obj.getString("id")));
                product.setName(json_obj.getString("name"));
                product.setDescription(json_obj.getString("description"));
                product.setPrice(Double.parseDouble(json_obj.getString("price")));
                product.setSalePrice(Double.parseDouble(json_obj.getString("sale_price")));
                Log.v(getClass().getName(), "The image URI is: "+ json_obj.getString("image_uri"));
                product.setImageUri(json_obj.getString("image_uri"));
                result.add(product);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String mockData =
            "{" +
                    "\"products\": [" +
                    "{" +
                    " \"id\": \"1\"," +
                    " \"name\": \"Logitech Mouse\"," +
                    " \"description\": \"Logitech Mouse with extensive features\"," +
                    " \"price\": \"100\"," +
                    " \"sale_price\": \"150\"," +
                    " \"image_uri\": \"mouse\"" +
                    "}," +
                    "{" +
                    " \"id\": \"2\"," +
                    " \"name\": \"LG Keyboard\"," +
                    " \"description\": \"LG Keyboard with extensive features\"," +
                    " \"price\": \"200\"," +
                    " \"sale_price\": \"400\"," +
                    " \"image_uri\": \"keyboard\"" +
                    "}," +
                    "{" +
                    " \"id\": \"3\"," +
                    " \"name\": \"Asus Monitor\"," +
                    " \"description\": \"Asus Monitor with extensive features\"," +
                    " \"price\": \"500\"," +
                    " \"sale_price\": \"600\"," +
                    " \"image_uri\": \"monitor\"" +
                    "}" +
                    "]" +
                    "}";
}
