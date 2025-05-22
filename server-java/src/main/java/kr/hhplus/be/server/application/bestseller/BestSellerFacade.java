package kr.hhplus.be.server.application.bestseller;

import kr.hhplus.be.server.domain.bestseller.BestSellerService;
import kr.hhplus.be.server.domain.bestseller.SalesStat;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BestSellerFacade {

    private final BestSellerService bestSellerService;
    private final ProductService productService;
    private final OrderService orderService;

    @Transactional(readOnly = true)
    public List<GetBestSellerResult> getTodayBestSellers() {
        return bestSellerService.getTop5Last3DaysSalesStat().stream()
                .map(it -> {
                    var product = productService.getProductById(it.getProductId());
                    return GetBestSellerResult.of(it, product);
                })
                .toList();
    }

    public void updateDailyBastSeller(UpdateBestSellerCommand command) {
        var order = orderService.getOrderById(command.orderId());
        order.getOrderItems().forEach(it -> bestSellerService.updateLast3DaysSalesStat(SalesStat.of(it)));
    }
}
