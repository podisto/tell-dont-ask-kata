package it.gabrieletondi.telldontaskkata.domain;

import it.gabrieletondi.telldontaskkata.useCase.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.*;

public class Order {
    private BigDecimal total;
    private final String currency;
    private final List<OrderItem> items;
    private BigDecimal tax;
    private OrderStatus status;
    private int id;

    public Order() {
        this.status = CREATED;
        this.items = new ArrayList<>();
        this.currency = "EUR";
        this.total = new BigDecimal("0.00");
        this.tax = new BigDecimal("0.00");
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getCurrency() {
        return currency;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void validate() {
        if (isCreatedOrRejected()) {
            throw new OrderCannotBeShippedException();
        }

        if (isShipped()) {
            throw new OrderCannotBeShippedTwiceException();
        }
    }

    public void approve(boolean isApproved) {
        if (isShipped()) {
            throw new ShippedOrdersCannotBeChangedException();
        }

        if (isApproved && isRejected()) {
            throw new RejectedOrderCannotBeApprovedException();
        }

        if (!isApproved && isApproved()) {
            throw new ApprovedOrderCannotBeRejectedException();
        }

        this.status = isApproved ? APPROVED : REJECTED;
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
        this.total = this.total.add(item.getTaxedAmount());
        this.tax = this.tax.add(item.getTax());
    }

    private boolean isCreatedOrRejected() {
        return isCreated() || isRejected();
    }

    private boolean isCreated() {
        return status.equals(CREATED);
    }

    private boolean isRejected() {
        return status.equals(REJECTED);
    }

    private boolean isShipped() {
        return status.equals(SHIPPED);
    }

    private boolean isApproved() {
        return status.equals(APPROVED);
    }
}
