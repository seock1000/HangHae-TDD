package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.order.command.CreateOrderCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderIdGenerator orderIdGenerator;
    @Mock
    private OrderCancelHandler orderCancelHandler;
    @InjectMocks
    private OrderService orderService;

    /**
     * 값 연산은 도메인 모델에서 검증했기때문에 mock 만 진행
     */
    @Test
    @DisplayName("주문 생성 시 유효한 CreateOrderCommand를 전달하면 주문과 주문 아이템을 생성하고 저장한다.")
    void createOrderTest() {
    }

}