package com.woi.merlin.ui.drawer;

public class DrawerItem {

    int drawerId;
    String itemName;
    int imgResID;
    String title;
    DrawerItemType type;

    public DrawerItem(int drawerId, String itemName, int imgResID) {
        super();
        this.drawerId = drawerId;
        this.itemName = itemName;
        this.imgResID = imgResID;
        type = DrawerItemType.TextWithImage;
    }

    public DrawerItem(int drawerId, DrawerItemType type) {
        this(drawerId, null, 0);
        this.type = type;
    }

    public DrawerItem(int drawerId, String title) {
        this(drawerId, null, 0);
        this.title = title;
        type = DrawerItemType.Text;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {

        this.itemName = itemName;
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

    public int getDrawerId() {
        return drawerId;
    }

    public void setDrawerId(int drawerId) {
        this.drawerId = drawerId;
    }
}
