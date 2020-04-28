package edu.uga.cs.roomatesapp.models;

public class PurchasedListItem {
    private int itemId;
    private String itemName;
    private String purchasedBy;
    private double price;

   public PurchasedListItem(String itemName, double price, String purchasedBy){
       this.itemName = itemName;
       this.price = price;
       this.purchasedBy = purchasedBy;
   }
   public PurchasedListItem(){}


    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPurchasedBy() {
        return purchasedBy;
    }

    public void setPurchasedBy(String purchasedBy) {
        this.purchasedBy = purchasedBy;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
