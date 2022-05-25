package com.company;

public class OutOfStockException extends Exception{
    private int quantity;

    public OutOfStockException(String message, int quantity) {
        super(message);
        this.quantity = quantity;
    }

    public OutOfStockException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "OutOfStockException{" +
                "quantity=" + quantity +
                "} " + super.toString();
    }
}