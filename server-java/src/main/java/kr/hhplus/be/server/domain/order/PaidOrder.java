package kr.hhplus.be.server.domain.order;

public class PaidOrder {
    private Orders order;

    private PaidOrder(Orders order) {
        this.order = order;
    }

    protected static PaidOrder of(Orders order) {
        return new PaidOrder(order);
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
