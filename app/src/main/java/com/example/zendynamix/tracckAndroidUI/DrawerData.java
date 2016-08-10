package com.example.zendynamix.tracckAndroidUI;

/**
 * Created by zendynamix on 7/8/2016.
 */
public class DrawerData {
    private  String menuItemText;
    private int menuIcon;

    public DrawerData(String menu, int icon){
        menuItemText=menu;
        menuIcon=icon;
    }

    public int getMenuIcon() {return menuIcon;}

    public void setMenuIcon(int menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getMenuItemText() {
        return menuItemText;
    }

    public void setMenuItemText(String menuItemText) {
        this.menuItemText = menuItemText;
    }
}
