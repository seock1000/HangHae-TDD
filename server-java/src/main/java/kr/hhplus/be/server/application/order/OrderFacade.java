package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.Orders;
import kr.hhplus.be.server.domain.order.command.CancelOrderHandlerCommand;
import kr.hhplus.be.server.domain.order.command.CreateOrderCommand;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.product.command.DecreaseStockCommand;
import kr.hhplus.be.server.domain.user.GetUserCommand;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final UserService userService;
    private final ProductService productService;

    /**
     * 주문 Facade
     * TC
     * 주문 흐름만 테스트 - mock
     */
    public OrderResult placeOrder(PlaceOrderCommand command) {

        List<CreateOrderCommand.OrderItemSpec> orderItemSpecs =
                command.orderItemSpecs().stream().map(spec -> {
                    // 상품 재고 차감
                    DecreaseStockCommand stockCmd = new DecreaseStockCommand(spec.productId(), spec.quantity());
                    Product sold = productService.decreaseStock(stockCmd);
                    // 주문 아이템 커멘드 생성
                    return new CreateOrderCommand.OrderItemSpec(spec.productId(), sold.getPrice(), spec.quantity());
                }).toList();

        // 유저 조회
        GetUserCommand userCmd = new GetUserCommand(command.userId());
        User user = userService.getUserById(userCmd);

        // 주문 생성
        CreateOrderCommand orderCmd = new CreateOrderCommand(user.getId(), orderItemSpecs);
        Orders order = orderService.createOrder(orderCmd);
        return OrderResult.of(order);
    }

    /**
     * 미결제 주문 취소 Facade
     * TC
     * 주문 취소 흐름만 테스트 - mock
     */
    public void cancelOrderByHandler(CancelOrderHandlerCommand command) {
        orderService.cancelByHandler(command);
    }
}
