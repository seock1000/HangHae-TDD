package kr.hhplus.be.server.presentation.bestseller;

import kr.hhplus.be.server.application.bestseller.UpdateBestSellerCommand;
import kr.hhplus.be.server.domain.order.OrderEvent;

import java.util.List;

public class BestSellerMapper {

    public static List<UpdateBestSellerCommand> from(OrderEvent.Confirmed event) {
        return event.items().stream()
                .map(item -> new UpdateBestSellerCommand(
                        item.productId(),
                        item.quantity()
                ))
                .toList();
    }
}

