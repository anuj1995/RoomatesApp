package edu.uga.cs.roomatesapp.models;

public class ShoppingListItem {
    private String itemName;
    private boolean purchased;
    private String itemId;

    public ShoppingListItem(){}
    public ShoppingListItem(String itemName,String itemId) {
        this.itemName = itemName; this.itemId =itemId;}

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }

}
