package com.pl.aerokrakmobile.model;

public class Cart {

    private String product_id, product_name, price, quantity, discount;

    public Cart() {
    }

    public Cart(String product_id, String productName, String price, String quantity, String discount) {
        this.product_id = product_id;
        this.product_name = productName;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
    }

    public String getProductId() {
        return product_id;
    }

    public void setProductId(String productId) {
        this.product_id = productId;
    }

    public String getProductName() {
        return product_name;
    }

    public void setProductName(String productName) {
        this.product_name = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
