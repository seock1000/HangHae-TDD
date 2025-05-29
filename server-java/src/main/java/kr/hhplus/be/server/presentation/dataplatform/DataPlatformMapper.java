package kr.hhplus.be.server.presentation.dataplatform;

import kr.hhplus.be.server.domain.dataplatform.OrderData;
import kr.hhplus.be.server.domain.order.OrderEvent;

import java.util.List;

public class DataPlatformMapper {

    public static List<OrderData> from(OrderEvent.Confirmed event) {
        return event.items().stream()
                .map(item -> new OrderData(
                        event.userId(),
                        item.productId(),
                        item.quantity(),
                        item.price()
                ))
                .toList();
    }
}
