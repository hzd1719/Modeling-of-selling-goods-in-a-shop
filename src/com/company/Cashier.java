package com.company;

import java.math.BigDecimal;

public class Cashier implements Comparable<Cashier>{

    private String name;
    private final long id;
    private BigDecimal salary;

    public Cashier(String name, long id, BigDecimal salary) {
        this.name = name;
        this.id = id;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "Cashier{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", salary=" + salary +
                '}';
    }

    @Override
    public int compareTo(Cashier o) {
        if(this.id > o.id) {
            return 1;
        }

        else if(this.id < o.id) {
            return -1;
        }
        return 0;
    }
}
