package com.whoopeelab.products;

/**
 * Created by grishmashah on 9/19/14.
 */
public class AddProductRowItem {

    private boolean isChecked;
    private String name;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AddProductRowItem(boolean isChecked, String name){
       this.isChecked = isChecked;
       this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
