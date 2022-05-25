package com.company;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Receipt {
    private long id;
    //private List<Goods> goodsList;
    private List<PairGoodQuantity> goodsList;
    private CashRegister cashRegister;

    private static long numberOfReceipts = 0;

    public Receipt(CashRegister cashRegister) {
        this.cashRegister = cashRegister;

        this.goodsList = new ArrayList<>();

        numberOfReceipts++;
        this.id = numberOfReceipts;
    }

    public long getId() {
        return id;
    }

    public static long getNumberOfReceipts() {
        return numberOfReceipts;
    }

    public CashRegister getCashRegisterOfRecipe() {
        return cashRegister;
    }

    public String getIssueTimeString() {
        LocalDateTime now = LocalDateTime.now();
        String formatTime = now.getHour() + ":" + now.getMinute();
        return formatTime;
    }

    public String getIssueDateString() {
        LocalDate now = LocalDate.now();
        return String.valueOf(now);
    }

    //public void addGoodToReceipt(Goods good) {
     //   goodsList.add(good);
    //}

    public void addGoodToReceipt(Goods good, int quantity) {
        goodsList.add(new PairGoodQuantity(good, quantity));
    }
    public BigDecimal getTotalPrice(BigDecimal decreaseInPercentage, int days) {
        BigDecimal sum = BigDecimal.ZERO;
        for(PairGoodQuantity pair : goodsList) {
            //sum+= good.sellingPrice(decreaseInPercentage, days);
            sum = sum.add(pair.getGood().sellingPrice(decreaseInPercentage, days).multiply(BigDecimal.valueOf(pair.getQuantity())));
        }

        return sum;
    }

    public List <String> printGoods(BigDecimal decreaseInPercentage, int days) {
        List<String> strs = new ArrayList<>();
        for(PairGoodQuantity pair : goodsList) {
            //System.out.println(good + " " + good.sellingPrice(decreaseInPercentage, days));
            strs.add(pair.getGood() + " price: " + pair.getGood().sellingPrice(decreaseInPercentage, days) + " quantity: " + pair.getQuantity() + System.lineSeparator() );
        }

        return strs;
    }

    public void saveReceipt(BigDecimal decreaseInPercentage, int days) {
        try(FileWriter fout = new FileWriter("files/receipts/receiptN" + this.getId() + ".txt")) {
            //BufferedWriter writer = new BufferedWriter(fout);
            fout.write(String.valueOf("Receipt number: " + this.getId() + System.lineSeparator()) +
                    "Issue date: " + this.getIssueDateString() + System.lineSeparator() +
                    "Issue time: " + String.valueOf(this.getIssueTimeString() + System.lineSeparator()) +
                    "Cashier: " + cashRegister.getCashier().getName() + " N:" + cashRegister.getCashier().getId() + System.lineSeparator() +
                    printGoods(decreaseInPercentage, days) +  System.lineSeparator() +
                    "Total: " + String.valueOf(getTotalPrice(decreaseInPercentage, days))

            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "id=" + id +
                ", goodsList=" + goodsList +
                ", cashRegister=" + cashRegister +
                '}';
    }
}
