package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.PointConstant;
import io.hhplus.tdd.point.domain.error.ExceedMaxPointError;
import org.apache.catalina.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.format.annotation.NumberFormat;

import java.io.PipedInputStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PointServiceImplTest {

    @Mock
    private UserPointTable userPointTable;
    @Mock
    private PointHistoryTable pointHistoryTable;
    @InjectMocks
    private PointServiceImpl pointService;


    /**
     * 유저 포인트 충전 TC - 성공
     * 유저 포인트를 충전할 때, 올바른 값이 충전되고 충전내역이 올바르게 전달되는지 검증합니다.
     */
    @Test
    @DisplayName("유저 포인트 충전 테스트 - 성공")
    void chargeUserPointTest() {
        //given
        long givenUserId = 1L;
        long givenChargeAmount = PointConstant.MAX_POINT;

        long expectedUserId = givenUserId;
        long expectedChargeAmount = givenChargeAmount;
        TransactionType expectedTransactionType = TransactionType.CHARGE;

        Mockito.when(userPointTable.selectById(givenUserId))
                .thenReturn(new UserPoint(1L, 0, System.currentTimeMillis()));
        Mockito.when(userPointTable.insertOrUpdate(givenUserId, givenChargeAmount))
                .thenReturn(new UserPoint(givenUserId, givenChargeAmount, System.currentTimeMillis()));

        ArgumentCaptor<Long> userIdCapture = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> chargeAmountCapture = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> historyUserIdCapture = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> historyChargeAmountCapture = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TransactionType> historyTransactionTypeCapture = ArgumentCaptor.forClass(TransactionType.class);

        //when
        UserPoint actualUserPoint = pointService.chargeUserPoint(givenUserId, givenChargeAmount);

        //then
        assertThat(actualUserPoint.id()).isEqualTo(expectedUserId);
        assertThat(actualUserPoint.point()).isEqualTo(expectedChargeAmount);

        Mockito.verify(userPointTable).insertOrUpdate(userIdCapture.capture(), chargeAmountCapture.capture());
        assertThat(userIdCapture.getValue()).isEqualTo(expectedUserId);
        assertThat(chargeAmountCapture.getValue()).isEqualTo(expectedChargeAmount);

        Mockito.verify(pointHistoryTable).insert(
                historyUserIdCapture.capture(),
                historyChargeAmountCapture.capture(),
                historyTransactionTypeCapture.capture(),
                Mockito.anyLong()
        );
        assertThat(historyUserIdCapture.getValue()).isEqualTo(expectedUserId);
        assertThat(historyChargeAmountCapture.getValue()).isEqualTo(expectedChargeAmount);
        assertThat(historyTransactionTypeCapture.getValue()).isEqualTo(expectedTransactionType);
    }


    /**
     * 유저 포인트 충전 TC - 실패
     * 포인트는 최대 액수를 넘으면 충전에 실패한다.
     */
    @Test
    @DisplayName("유저 포인트 충전 테스트 - 포인트는 최대 액수를 넘으면 충전에 실패한다.")
    void chargeUserPointExceedMax() {
        //given
        long givenUserId = 1L;
        long givenChargeAmount = PointConstant.MAX_POINT + 1;

        Mockito.when(userPointTable.selectById(Mockito.any())).thenReturn(new UserPoint(1L, 0, System.currentTimeMillis()));

        //when & then
        assertThatThrownBy(() -> pointService.chargeUserPoint(givenUserId, givenChargeAmount))
                .isInstanceOf(ExceedMaxPointError.class);
    }
}