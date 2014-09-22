package com.whoopeelab.products.product;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by grishmashah on 9/18/14.
 */
public class ProductSqLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "productDB.db";
    public static final String TABLE_PRODUCTS = "products";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_REG_PRICE = "price";
    public static final String COLUMN_SALE_PRICE = "sale_price";
    public static final String COLUMN_IMG_URI = "image_uri";

    private static ProductSqLiteHelper productSqLiteHelper = null;

    private static final String[] COLUMNS = {COLUMN_ID,COLUMN_NAME,COLUMN_DESC,COLUMN_REG_PRICE,COLUMN_SALE_PRICE,COLUMN_IMG_URI};

    private ProductSqLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static ProductSqLiteHelper getInstance(Context context){
        if (productSqLiteHelper == null) {
            synchronized (ProductSqLiteHelper.class) {
                if (productSqLiteHelper == null) {
                    productSqLiteHelper = new ProductSqLiteHelper(context);
                }
            }
        }
        return productSqLiteHelper;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_PRODUCTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_DESC + " TEXT,"
                + COLUMN_REG_PRICE + " INTEGER,"
                + COLUMN_SALE_PRICE + " INTEGER,"
                + COLUMN_IMG_URI + " TEXT"
                + ")";
        Log.v(getClass().getName(),"Creating table" + CREATE_PRODUCTS_TABLE);
        sqLiteDatabase.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(sqLiteDatabase);
    }

    public boolean addProduct(Product product) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        boolean isAdded = true;
        try {
        db = this.getReadableDatabase();
        cursor =
                db.query(TABLE_PRODUCTS, // a. table
                        COLUMNS, // b. column names
                        " _id = ?", // c. selections
                        new String[] { String.valueOf(product.getId()) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null);

            if (cursor != null && cursor.moveToFirst()) {
                isAdded = false;
                cursor.close();
            }else {
                db = this.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(COLUMN_ID, product.getId());
                values.put(COLUMN_NAME, product.getName());
                values.put(COLUMN_DESC, product.getDescription());
                values.put(COLUMN_REG_PRICE, product.getPrice());
                values.put(COLUMN_SALE_PRICE, product.getSalePrice());
                values.put(COLUMN_SALE_PRICE, product.getSalePrice());
                values.put(COLUMN_IMG_URI, product.getImageUri());

                db.insert(TABLE_PRODUCTS, null, values);
                Log.v(getClass().getName(), "Data Added to the database");
            }
        } catch (SQLiteConstraintException e) {
                Log.v(getClass().getName(), "SQLiteConstraintException: "+ e.toString());
        }catch (SQLException e){

        }finally {
            cursor.close();
            db.close();
        }
        return isAdded;
    }

    public void updateProduct(Product product) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_DESC, product.getDescription());
        values.put(COLUMN_REG_PRICE, product.getPrice());
        values.put(COLUMN_SALE_PRICE, product.getSalePrice());

        db.update(TABLE_PRODUCTS,values,
                COLUMN_ID+" = ?", // selections
                new String[] { String.valueOf(product.getId()) });

        db.close();

    }

    public void deleteProduct(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_PRODUCTS,
                COLUMN_ID+" = ?",
                new String[] { String.valueOf(id) });

        db.close();
    }

    public Product getProduct(String id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =
                db.query(TABLE_PRODUCTS, // a. table
                        COLUMNS, // b. column names
                        " _id = ?", // c. selections
                        new String[] { id }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null);

        if (cursor != null)
            cursor.moveToFirst();

        Product prod = new Product();
        prod.setId(Double.parseDouble(cursor.getString(0)));
        prod.setName(cursor.getString(1));
        prod.setDescription(cursor.getString(2));
        prod.setPrice(Double.parseDouble(cursor.getString(3)));
        prod.setSalePrice(Double.parseDouble(cursor.getString(4)));
        prod.setImageUri(cursor.getString(5));

        cursor.close();

        return prod;
    }

    public List<Product> getAllProducts() {

        List products = new ArrayList<Product>();

        String query = "SELECT  * FROM " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Product prod =null;
        if (cursor.moveToFirst()) {
            do {
                prod = new Product();
                prod.setId(Double.parseDouble(cursor.getString(0)));
                prod.setName(cursor.getString(1));
                prod.setDescription(cursor.getString(2));
                prod.setPrice(Double.parseDouble(cursor.getString(3)));
                prod.setSalePrice(Double.parseDouble(cursor.getString(4)));
                prod.setImageUri(cursor.getString(5));

                // Add book to books
                products.add(prod);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return products;
    }

    public Cursor getAllProductsWithCursor() {

        List products = new ArrayList<Product>();

        String query = "SELECT  * FROM " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }
}
