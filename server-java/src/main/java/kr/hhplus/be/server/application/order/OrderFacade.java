package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.Orders;
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
     * 주문 흐름만 테스트
     */
    public OrderResult placeOrder(PlaceOrderCommand command) {

        List<CreateOrderCommand.OrderItemSpec> orderItemSpecs =
                command.orderItemSpecs().stream().map(orderItemSpec -> {
                    DecreaseStockCommand productCmd = new DecreaseStockCommand(orderItemSpec.productId(), orderItemSpec.quantity());
                    Product soldProduct = productService.decreaseStock(productCmd);
                    return new CreateOrderCommand.OrderItemSpec(
                            orderItemSpec.productId(),
                            orderItemSpec.quantity(),
                            soldProduct.getPrice()
                    );
                }).toList();

        GetUserCommand userCmd = new GetUserCommand(command.userId());
        User user = userService.getUserById(userCmd);

        CreateOrderCommand orderCmd = new CreateOrderCommand(user.getId(), orderItemSpecs);
        Orders order = orderService.createOrder(orderCmd);
        return OrderResult.of(order);
    }
}
