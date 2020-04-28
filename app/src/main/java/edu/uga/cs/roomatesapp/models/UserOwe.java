package edu.uga.cs.roomatesapp.models;

public class UserOwe {
    private String username;
    private double amount;

    public UserOwe(){}
    public UserOwe(String username){
        this.username =username ;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
