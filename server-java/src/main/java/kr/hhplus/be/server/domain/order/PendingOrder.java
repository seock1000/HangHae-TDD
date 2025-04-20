package kr.hhplus.be.server.domain.order;

public class PendingOrder {
    private Orders order;

    private PendingOrder(Orders order) {
        this.order = order;
    }

    protected static PendingOrder of(Orders order) {
        return new PendingOrder(order);
    }

    public String getId() {
        return order.getId();
    }

    public int getTotalAmount() {
        return order.getTotalAmount();
    }

    public void confirm() {
        order.confirm();
    }
}
