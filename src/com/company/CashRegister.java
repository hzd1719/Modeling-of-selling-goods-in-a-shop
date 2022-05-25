package com.company;

import java.util.ArrayList;
import java.util.List;

public class CashRegister implements  Comparable<CashRegister>{

    private Cashier cashier;
    private final int id;
    private List<Customer> customersQue;

    public CashRegister(int id) {
        this.cashier = null;
        this.id = id;
        this.customersQue = new ArrayList<>();
    }

    /*public CashRegister(Cashier cashier, int id) {
        this.cashier = cashier;
        this.id = id;
        this.customersQue = new ArrayList<>();
    }*/


    public int getCustomerListSize() {
        return customersQue.size();
    }

    public void addCustomer(Customer customer) {
        if(cashier != null) {
            customersQue.add(customer);
        }
        else {
            System.out.println("The cash register is closed! N:" + this.getId());
        }
    }
    public Customer removeCustomerIndex() {
        return customersQue.remove(0);
    }

    public void removeCustomer(Customer customer) {
        customersQue.remove(customer);
    }

    public Cashier getCashier() {
        return cashier;
    }

    public int getId() {
        return id;
    }

    public void setCashier(Cashier cashier) {
        this.cashier = cashier;
    }

    public void removeCashier() {
        this.cashier = null;
    }

    @Override
    public String toString() {
        return "CashRegister{" +
                "cashier=" + cashier +
                ", id=" + id +
                '}';
    }

    @Override
    public int compareTo(CashRegister o) {
        if(this.id > o.id) {
            return 1;
        }

        else if(this.id < o.id) {
            return -1;
        }
        return 0;
    }
}
