package com.woi.merlin.ui.sidedrawer;

/**
 * Created by Jeffery on 12/30/2014.
 */
public class DrawerItem {

    String ItemName;
    int imgResID;
    String title;
    boolean isSpinner;

    public DrawerItem(String itemName, int imgResID) {
        super();
        ItemName = itemName;
        this.imgResID = imgResID;
    }

    public DrawerItem(boolean isSpinner) {
        this(null, 0);
        this.isSpinner = isSpinner;
    }

    public DrawerItem(String title) {
        this(null, 0);
        this.title = title;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getImgResID() {
        return imgResID;
    }

    public void setImgResID(int imgResID) {
        this.imgResID = imgResID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSpinner() {
        return isSpinner;
    }

    public void setSpinner(boolean isSpinner) {
        this.isSpinner = isSpinner;
    }
}
