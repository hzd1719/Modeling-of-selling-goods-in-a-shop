package com.company;

public class PairGoodQuantity<F, S> {
    private final Goods good; //first member of pair
    private int quantity; //second member of pair

    public PairGoodQuantity(Goods good, int quantity) {
        this.good = good;
        this.quantity = quantity;
    }


    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Goods getGood() {
        return good;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "PairGoodQuantity{" +
                "good=" + good +
                ", quantity=" + quantity +
                '}';
    }
}