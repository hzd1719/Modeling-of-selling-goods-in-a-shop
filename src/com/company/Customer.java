package com.company;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String name;
    private BigDecimal money;
    private List<PairGoodQuantity> goodsToBuy;

    public Customer(BigDecimal money) {
        this.money = money;
        this.name = " ";
        this.goodsToBuy = new ArrayList<>();
    }

    public Customer(String name, BigDecimal money) {
        this.name = name;
        this.money = money;
        this.goodsToBuy = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public List<PairGoodQuantity> getGoodsToBuy() {
        return goodsToBuy;
    }

    public int getGoodsToBuySize() {
        return goodsToBuy.size();
    }

    public void addGoodToCustomer(Goods goods, int quantity) {
        if(quantity < 0) {
            throw new IllegalArgumentException("Can't buy negative quantity! " + quantity);
        }
        goodsToBuy.add(new PairGoodQuantity(goods, quantity));
    }

    public BigDecimal decreaseMoney(BigDecimal decreaseWith) {
        //money = money - decreaseWith;
        if(decreaseWith.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Can't decrease money with negative! " + decreaseWith);
        }
        money = money.subtract(decreaseWith);
        return money;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", money=" + money +
                ", goodsToBuy=" + goodsToBuy +
                '}';
    }
}
