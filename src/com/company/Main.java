package com.company;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {

    public static void main(String[] args) {
	// write your code here
        LocalDate expDate1 = LocalDate.of(2023, 1, 30);
        LocalDate expDate2 = LocalDate.of(2022, 1, 30);
        LocalDate expDate3 = LocalDate.of(2022, 5, 26);
        LocalDate expDate4 = LocalDate.of(2022, 5, 20);

        Goods goods1 = new Goods("Qbalka", BigDecimal.valueOf(50), GoodsType.GROCERIES, expDate1);
        Goods goods2 = new Goods("Stol", BigDecimal.valueOf(10), GoodsType.ITEMS, expDate1);
        Goods goods3 = new Goods("Mlqko", BigDecimal.valueOf(20), GoodsType.GROCERIES, expDate1);
        Goods goods4 = new Goods("Masa", BigDecimal.valueOf(200), GoodsType.ITEMS, expDate3);
        //Goods goods5 = new Goods("Qica", BigDecimal.valueOf(-30), GoodsType.GROCERIES, expDate4);


        Cashier cashier1 = new Cashier("Vanq", 1, BigDecimal.valueOf(300));
        Cashier cashier2 = new Cashier("Gogo", 2, BigDecimal.valueOf(600));
        Cashier cashier3 = new Cashier("Kinq", 6, BigDecimal.valueOf(500));
        Cashier cashier4 = new Cashier("Minchu", 5, BigDecimal.valueOf(100));

        Customer customer1 = new Customer("Steven", BigDecimal.valueOf(1000));
        Customer customer2 = new Customer("Piratko", BigDecimal.valueOf(10000));
        Customer customer3 = new Customer("Gankplank", BigDecimal.valueOf(800));
        Customer customer4 = new Customer("Sona", BigDecimal.valueOf(1000));
        Customer customer5 = new Customer("Brand", BigDecimal.valueOf(200));

        CashRegister cashRegister1 = new CashRegister(1);
        CashRegister cashRegister2 = new CashRegister(2);
        CashRegister cashRegister3 = new CashRegister(3);
        CashRegister cashRegister4 = new CashRegister(4);
        CashRegister cashRegister5 = new CashRegister(5);

        Shop shop = new Shop(BigDecimal.valueOf(20), BigDecimal.valueOf(20), BigDecimal.valueOf(20), 5);
        shop.addGoods(goods1, 30);
        shop.addGoods(goods2, 20);
        shop.addGoods(goods3, 10);
        shop.addGoods(goods4, 40);

        shop.addCashRegister(cashRegister1);
        shop.addCashRegister(cashRegister2);
        shop.addCashRegister(cashRegister3);
        shop.addCashRegister(cashRegister4);
        shop.addCashRegister(cashRegister5);

        shop.addCashier(cashier1);
        shop.addCashier(cashier2);
        shop.addCashier(cashier3);
        shop.addCashier(cashier4);

        shop.addCustomer(customer1);
        shop.addCustomer(customer2);
        shop.addCustomer(customer3);
        shop.addCustomer(customer4);
        shop.addCustomer(customer5);

        shop.setCashierToCashRegister(cashRegister1, cashier1);
        shop.setCashierToCashRegister(cashRegister2, cashier2);
        shop.setCashierToCashRegister(cashRegister3, cashier3);
        shop.setCashierToCashRegister(cashRegister5, cashier4);


        customer1.addGoodToCustomer(goods1, 10);
        customer1.addGoodToCustomer(goods3, 5);

        customer3.addGoodToCustomer(goods2, 10);
        customer3.addGoodToCustomer(goods1, 8);

        customer4.addGoodToCustomer(goods1, 10);
        customer4.addGoodToCustomer(goods2, 5);
        customer4.addGoodToCustomer(goods3, 3);

        customer5.addGoodToCustomer(goods1, 1);
        customer5.addGoodToCustomer(goods4, 2);

        customer2.addGoodToCustomer(goods4, 5);



        shop.addCustomerToCashRegister(customer1, cashRegister1);
        shop.addCustomerToCashRegister(customer3, cashRegister2);
        shop.addCustomerToCashRegister(customer4, cashRegister3);
        shop.addCustomerToCashRegister(customer5, cashRegister5);
        shop.addCustomerToCashRegister(customer2, cashRegister5);


        shop.startSelling();


    }
}
