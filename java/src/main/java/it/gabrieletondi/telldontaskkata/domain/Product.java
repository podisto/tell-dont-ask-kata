package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

public class Product {
    private String name;
    private BigDecimal price;
    private Category category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getTaxedAmount(int quantity) {
        BigDecimal unitaryTaxedAmount = getUnitaryTaxedAmount();
        return unitaryTaxedAmount.multiply(BigDecimal.valueOf(quantity))
                .setScale(2, HALF_UP);
    }

    public BigDecimal getTaxAmount(int quantity) {
        BigDecimal unitaryTax = getUnitaryTax();
        return unitaryTax.multiply(BigDecimal.valueOf(quantity));
    }

    private BigDecimal getUnitaryTax() {
        return price.divide(valueOf(100))
                .multiply(category.getTaxPercentage())
                .setScale(2, HALF_UP);
    }

    private BigDecimal getUnitaryTaxedAmount() {
        BigDecimal unitaryTax = getUnitaryTax();
        return price.add(unitaryTax).setScale(2, HALF_UP);
    }
}
