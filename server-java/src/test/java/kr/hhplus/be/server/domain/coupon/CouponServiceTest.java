package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.domain.user.User;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;
    @InjectMocks
    private CouponService couponService;

    @Test
    @DisplayName("쿠폰 발급 시, 이미 해당 쿠폰을 발급 받았다면 ApiException(COUPON_ALREADY_ISSUED)이 발생한다.")
    void issueCoupon_AlreadyIssued() {
        // given
        User user = Instancio.create(User.class);
        Coupon coupon = Instancio.create(Coupon.class);
        when(couponRepository.existsUserCouponByUserAndCoupon(user, coupon)).thenReturn(true);

        // when, then
        ApiException exception = assertThrows(ApiException.class, () -> {
            couponService.issueCoupon(user, coupon);
        });
        assertEquals(ApiError.COUPON_ALREADY_ISSUED, exception.getApiError());
    }
}