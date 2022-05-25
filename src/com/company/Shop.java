package com.company;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Shop {

    private List<PairGoodQuantity> deliveredGoods;
    private List<PairGoodQuantity> soldGoods;
    private List<Cashier> cashiersWorkers; //Spisak s kasierite v magazina
    private List<Cashier> busyCashiers; //Spisak s kasiertie, koito zaemat kasa v momenta
    private List<Receipt> issuedReceipts; //Spisak s izdadenite kasovi belejki
    private List<CashRegister> cashRegisters;
    private List<Customer> customers;

    private BigDecimal profitGoods;
    private BigDecimal markupGroceries; //Nadcenka hranitelni stoki v procent
    private BigDecimal markupItems; //Nadcenka nehtranitelni stoki v procent
    private BigDecimal saleBeforeExpire;//Kolko e promociqta ot dni predi iztichaneto v procent
    private int daysBeforeExpire; //Kolko dni predi iztichaneto na godnostta na produkta za da se sloji promociqta

    public Shop(BigDecimal markupGroceries, BigDecimal markupItems, BigDecimal saleBeforeExpire, int daysBeforeExpire) {
        this.profitGoods = BigDecimal.ZERO;

        setMarkupGroceries(markupGroceries);
        setMarkupItems(markupItems);
        setSaleBeforeExpire(saleBeforeExpire);
        setDaysBeforeExpire(daysBeforeExpire);

        this.deliveredGoods = new ArrayList<>();
        this.soldGoods = new ArrayList<>();
        this.cashiersWorkers = new ArrayList<>();
        this.busyCashiers = new ArrayList<>();
        this.issuedReceipts = new ArrayList<>();
        this.cashRegisters = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        if(!customers.contains(customer)) {
            customers.add(customer);
        }
    }

    public void removeCustomer(Customer customer) {
        customers.remove(customer);
    }
    public void addCustomerToCashRegister(Customer customer, CashRegister cashRegister) {
        if(customers.contains(customer) && cashRegisters.contains(cashRegister) && customer.getGoodsToBuySize() > 0) {
            cashRegister.addCustomer(customer);
        }

        else {
            System.out.println("None existent customer or cash register!");
        }
    }

    public void setCashierToCashRegister(CashRegister cashRegister, Cashier cashier) {
        if((!cashiersWorkers.contains(cashier)) || (!cashRegisters.contains(cashRegister))) {
            System.out.println("None existent cashier or cash register!");
        }

        else {
            if (busyCashiers.contains(cashier)) {
                System.out.println("This cashier is busy now, pick another one!");
            }

            else {

                if (cashRegister.getCashier() != null) {
                    System.out.print(cashRegister.getCashier().getName() + "" +
                            " is being replaced by ");
                    freeCashRegister(cashRegister);
                }

                System.out.println(cashier.getName() + " on Cash register - " + cashRegister.getId());
                cashRegister.setCashier(cashier);
                busyCashiers.add(cashier);
            }
        }
    }

    public boolean freeCashRegister(CashRegister cashRegister) {
        if(cashRegister.getCashier() != null) {
            busyCashiers.remove(cashRegister.getCashier());
            cashRegister.removeCashier();
            return true;
        }

        return false;
    }

    public void addCashRegister(CashRegister cashRegister) {
        if(!cashRegisters.contains(cashRegister)) {
            cashRegisters.add(cashRegister);
        }
    }

    public void addCashier(Cashier cashier) {
        if(!cashiersWorkers.contains(cashier)) {
            cashiersWorkers.add(cashier);
        }
    }

    public void addGoods(Goods good, int quantity) {
        boolean inside = false;
        if(good.getGoodsType() == GoodsType.ITEMS) {
            good.setMarkUp(this.markupItems);
        }

        else {
            good.setMarkUp(this.markupGroceries);
        }
        for(PairGoodQuantity pairs : deliveredGoods) {
            if(pairs.getGood() == good) {
                inside = true;
                pairs.setQuantity(pairs.getQuantity() + quantity);
                break;
            }
        }

        if(!inside) {
            deliveredGoods.add(new PairGoodQuantity(good, quantity));
            soldGoods.add(new PairGoodQuantity(good, 0));
        }
    }

    private void addToSoldGoods(Goods good, int quantity) {
        for(PairGoodQuantity pairs : soldGoods) {
            if(pairs.getGood() == good) {
                pairs.setQuantity(pairs.getQuantity() + quantity);

            }
        }

        for(PairGoodQuantity pairs: deliveredGoods) {
            if(pairs.getGood() == good) {
                pairs.setQuantity(pairs.getQuantity() - quantity);
                if(pairs.getQuantity() <= 0) {
                    deliveredGoods.remove(pairs);
                }
            }
        }
    }

    private void sell(Goods goodForSale, int numberOfTheGoodToBuy, BigDecimal money) throws OutOfStockException {
        int quantityOfTheGood = countGood(goodForSale);
        if(quantityOfTheGood >= numberOfTheGoodToBuy) {
            if(!goodForSale.isExpired()) {
                if(money.compareTo(goodForSale.sellingPrice(saleBeforeExpire, daysBeforeExpire).multiply(BigDecimal.valueOf(numberOfTheGoodToBuy))) > -1) {
                    //System.out.println("Sell the good");
                    //profitGoods = profitGoods +
                           // goodForSale.sellingPrice(saleBeforeExpire, daysBeforeExpire) * numberOfTheGoodToBuy -
                            //goodForSale.getDeliveryPrice() * numberOfTheGoodToBuy;
                    profitGoods = profitGoods.add(goodForSale.sellingPrice(saleBeforeExpire, daysBeforeExpire).multiply(BigDecimal.valueOf(numberOfTheGoodToBuy))
                    .subtract(goodForSale.getDeliveryPrice().multiply(BigDecimal.valueOf(numberOfTheGoodToBuy))));
                    addToSoldGoods(goodForSale, numberOfTheGoodToBuy);
                }

                else {
                    System.out.println("The customer needs more money!");
                }
            }

            else {
                System.out.println("The good is expired and can't be sold: " + goodForSale);
            }
        }

        else {
            throw new OutOfStockException("We don't have the necessary quantity. Additional quantity needed to complete transaction: "
                    + (numberOfTheGoodToBuy - quantityOfTheGood), numberOfTheGoodToBuy - quantityOfTheGood);
        }
    }

    private synchronized void sellToCustomer(Customer customer, CashRegister cashRegister) {
        Receipt receipt = new Receipt(cashRegister);

        for(int i = 0; i < customer.getGoodsToBuy().size(); i++) {
            try {
                sell(customer.getGoodsToBuy().get(i).getGood(), customer.getGoodsToBuy().get(i).getQuantity(), customer.getMoney());
                customer.decreaseMoney(customer.getGoodsToBuy().get(i).getGood().sellingPrice(saleBeforeExpire, daysBeforeExpire).multiply(
                        BigDecimal.valueOf(customer.getGoodsToBuy().get(i).getQuantity())
                ));
                receipt.addGoodToReceipt(customer.getGoodsToBuy().get(i).getGood(), customer.getGoodsToBuy().get(i).getQuantity());
            } catch (OutOfStockException e) {
                e.printStackTrace();
            }
        }
        System.out.println(cashRegister.getCashier().getName() + " N: " + cashRegister.getCashier().getId()
                + " On cash register N: " + cashRegister.getId() + ", Total: " + receipt.getTotalPrice(saleBeforeExpire, daysBeforeExpire));
        issuedReceipts.add(receipt);
        receipt.saveReceipt(saleBeforeExpire, daysBeforeExpire);
        customers.remove(customer);
    }

    public void startSelling() {
        //Zashto vsichki kasi, nqkoi moje da sa v magazina, no da ne gi polzvasj????
        for(CashRegister cashRegister : cashRegisters) {
            if(cashRegister.getCashier() != null) {
                Runnable runnable = new Runnable() {

                    @Override
                    public void run() {
                        int s = cashRegister.getCustomerListSize();
                        for (int i = 0; i < s; i++) {
                            sellToCustomer(cashRegister.removeCustomerIndex(), cashRegister);

                        }
                    }
                };

                new Thread(runnable).start();
            }
        }
    }

    private int countGood(Goods goodToCount) {
        for(PairGoodQuantity pairs : deliveredGoods) {
            if(pairs.getGood() == goodToCount) {
                return pairs.getQuantity();
            }
        }

        return -1;
    }

    private BigDecimal expendituresForSalaries() {
        BigDecimal sum = BigDecimal.ZERO;
        for(Cashier cashier : cashiersWorkers) {
            //sum += cashier.getSalary();
            sum = sum.add(cashier.getSalary());
        }

        return sum;
    }

    public BigDecimal getProfitGoods() {
        return profitGoods;
    }

    public BigDecimal checkGoodsProfit() {
        BigDecimal sum = BigDecimal.ZERO;
        for(PairGoodQuantity pair : soldGoods) {
            //sum = sum + pair.getGood().sellingPrice(saleBeforeExpire, daysBeforeExpire) * pair.getQuantity()
              //      - pair.getGood().getDeliveryPrice() * pair.getQuantity();
            sum = sum.add(
                    (pair.getGood().sellingPrice(saleBeforeExpire, daysBeforeExpire).multiply(BigDecimal.valueOf(pair.getQuantity()))).subtract(
                            (pair.getGood().getDeliveryPrice().multiply(BigDecimal.valueOf(pair.getQuantity())))
                    ));
        }

        return sum;
    }

    public BigDecimal overallProfit() {
        return expendituresForSalaries().subtract(getProfitGoods());
    }

    public void setMarkupGroceries(BigDecimal markupGroceries) {
        if(markupGroceries.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("The mark up can't be negative!");
        }
        this.markupGroceries = markupGroceries;
    }

    public void setMarkupItems(BigDecimal markupItems) {
        if(markupItems.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("The mark up can't be negative!");
        }
        this.markupItems = markupItems;
    }

    public void setSaleBeforeExpire(BigDecimal saleBeforeExpire) {
        if(saleBeforeExpire.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Choose positive value for the sale!");
        }
        this.saleBeforeExpire = saleBeforeExpire;
    }

    public void setDaysBeforeExpire(int daysBeforeExpire) {
        if(daysBeforeExpire < 0) {
            throw new IllegalArgumentException("Days can't be negative");
        }
        this.daysBeforeExpire = daysBeforeExpire;
    }



    public BigDecimal getMarkupGroceries() {
        return markupGroceries;
    }

    public BigDecimal getMarkupItems() {
        return markupItems;
    }

    public int getDaysBeforeExpire() {
        return daysBeforeExpire;
    }

    public BigDecimal getSaleBeforeExpire() {
        return saleBeforeExpire;
    }


    @Override
    public String toString() {
        return "Shop{" +
                "deliveredGoods=" + deliveredGoods +
                ", markupGroceries=" + markupGroceries +
                ", markupItems=" + markupItems +
                ", daysBeforeExpire=" + daysBeforeExpire +
                ", saleBeforeExpire=" + saleBeforeExpire +
                '}';
    }
}
