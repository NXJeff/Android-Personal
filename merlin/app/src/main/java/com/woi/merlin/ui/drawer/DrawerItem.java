package com.woi.merlin.ui.drawer;

public class DrawerItem {

    String ItemName;
    int imgResID;
    String title;
    DrawerItemType type;

    public DrawerItem(String itemName, int imgResID) {
        super();
        ItemName = itemName;
        this.imgResID = imgResID;
        type = DrawerItemType.TextWithImage;
    }

    public DrawerItem(DrawerItemType type) {
        this(null, 0);
        this.type = type;
    }

    public DrawerItem(String title) {
        this(null, 0);
        this.title = title;
        type = DrawerItemType.Text;
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

    public DrawerItemType getType() {
        return type;
    }

    public void setType(DrawerItemType type) {
        this.type = type;
    }
}
