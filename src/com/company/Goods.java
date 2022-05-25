package com.company;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;

public class Goods {
    private final long id;

    private final String name;
    private BigDecimal deliveryPrice;
    private final GoodsType goodsType;
    private LocalDate expireDate;
    private BigDecimal markUp; //Opredelq se v Shop v zamisimot ot vidq stoka


    private static long numberOfGoods = 0;

    public Goods(String name, BigDecimal deliveryPrice, GoodsType goodsType, LocalDate expireDate) {
        this.name = name;
        this.goodsType = goodsType;
        setDeliveryPrice(deliveryPrice);
        setExpireDate(expireDate);

        numberOfGoods++;
        this.id = numberOfGoods;
    }

    //proverqva kolko dni ima mejdu dve dati
    private long daysBetweenTwoDates(LocalDate date1, LocalDate date2) {
        return  ChronoUnit.DAYS.between(date1, date2);
    }
    //Checks if the good is expired
    public boolean isExpired() {
        LocalDate today = LocalDate.now();
        return today.isAfter(this.expireDate);
    }

    //proverqva dali trqbva da se namali stokata - predi x broi dni(opredelen ot Shop) ot datata na iztichane sroka na godnost
    //days defined in Shop
    private boolean isTheGoodOnSaleBecauseItExpires(int days) {
        LocalDate today = LocalDate.now();
        long daysBetween = daysBetweenTwoDates(today, this.expireDate);
        return days >= daysBetween;
    }

    //Namlqva prodajnata cena na stokata s procent opredelen ot magazina, ako stokato izpalnqva gornoto uslovie za iztichane na sroka
    private BigDecimal decreaseSellingPrice(BigDecimal decreaseInPercentage, BigDecimal price) {

        //price = price - price * (decreaseInPercentage / 100);
        price = price.subtract(price.multiply((decreaseInPercentage.divide(BigDecimal.valueOf(100)))));
        return price;
    }

    //Vrashta prodajnata cena na stokata
    //decreaseInPercentage se oprdelq ot Shop
    //dni predi iztichaneto na sroka da godnost za da ima namalenie se opredelq ot Shop
    public BigDecimal sellingPrice(BigDecimal decreaseInPercentage, int days) {
        //MarkUp is in TypeOfFood and is defined in Shop depending on the type of food
        if(decreaseInPercentage.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Percentage can't be negative!");
        }

        if(days < 0) {
            throw new IllegalArgumentException("Days can't be negative!");
        }
        //double price = this.deliveryPrice + this.deliveryPrice * (this.goodsType.getMarkUpInPercentage()/100);
        BigDecimal price = this.deliveryPrice.add(this.deliveryPrice.multiply((this.getMarkUp().divide(BigDecimal.valueOf(100)))));
        if(!isTheGoodOnSaleBecauseItExpires(days)) {
            return price;
        }

        else {
            return decreaseSellingPrice(decreaseInPercentage, price);
        }
    }

    public BigDecimal getDeliveryPrice() {
        return deliveryPrice;
    }

    public long getGoodsId() {
        return id;
    }

    public String getGoodsName() {
        return name;
    }


    public LocalDate getExpireDate() {
        return expireDate;
    }

    public GoodsType getGoodsType() {
        return goodsType;
    }

    public static long getNumberOfGoods() {
        return numberOfGoods;
    }

    public BigDecimal getMarkUp() {
        return markUp;
    }

    public void setMarkUp(BigDecimal markUp) {
        if(markUp.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Mark up can't be negative! " + markUp);
        }

        else {
            this.markUp = markUp;
        }
    }

    public void setDeliveryPrice(BigDecimal deliveryPrice) throws IllegalArgumentException{
        if(deliveryPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price can't be negative " + deliveryPrice);
        }

        else {
            this.deliveryPrice = deliveryPrice;
        }
    }

    private void setExpireDate(LocalDate expireDate) throws IllegalArgumentException {
        if(expireDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Can't create goods that is already expired " + expireDate);
        }

        else {
            this.expireDate = expireDate;
        }
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", deliveryPrice=" + deliveryPrice +
                ", goodsType=" + goodsType +
                ", expireDate=" + expireDate +
                '}';
    }
}
