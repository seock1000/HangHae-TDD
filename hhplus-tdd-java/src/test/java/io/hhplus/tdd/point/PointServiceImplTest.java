package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.apache.catalina.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.format.annotation.NumberFormat;

import java.io.PipedInputStream;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PointServiceImplTest {

    private UserPointTable userPointTable;
    private PointHistoryTable pointHistoryTable;
    private PointServiceImpl pointService;

    @BeforeEach
    void beforeEach() {
        userPointTable = Mockito.mock(UserPointTable.class);
        pointHistoryTable = Mockito.mock(PointHistoryTable.class);

        pointService = new PointServiceImpl(
                userPointTable,
                pointHistoryTable
        );
    }

    /**
     * 유저 포인트 충전 TC - 성공
     * 유저 포인트를 충전할 때, 올바른 값이 충전되고 충전내역이 올바르게 전달되는지 검증합니다.
     */
    @Test
    @DisplayName("유저 포인트 충전 테스트 - 성공")
    void chargeUserPointTest() {
        //given
        long givenUserId = 1L;
        long givenChargeAmount = 100L;

        long expectedUserId = givenUserId;
        long expectedChargeAmount = givenChargeAmount;
        TransactionType expectedTransactionType = TransactionType.CHARGE;

        ArgumentCaptor<Long> userIdCapture = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> chargeAmountCapture = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> historyUserIdCapture = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> historyChargeAmountCapture = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TransactionType> historyTransactionTypeCapture = ArgumentCaptor.forClass(TransactionType.class);

        Mockito.verify(userPointTable).insertOrUpdate(userIdCapture.capture(), chargeAmountCapture.capture());
        Mockito.verify(pointHistoryTable).insert(
                historyUserIdCapture.capture(),
                historyChargeAmountCapture.capture(),
                historyTransactionTypeCapture.capture(),
                Mockito.any()
        );

        //when
        UserPoint actualUserPoint = pointService.chargeUserPoint(givenUserId, givenChargeAmount);

        //then
        assertThat(actualUserPoint.id()).isEqualTo(expectedUserId);
        assertThat(actualUserPoint.point()).isEqualTo(expectedChargeAmount);

        assertThat(userIdCapture.getValue()).isEqualTo(expectedUserId);
        assertThat(chargeAmountCapture.getValue()).isEqualTo(expectedChargeAmount);

        assertThat(historyUserIdCapture.getValue()).isEqualTo(expectedUserId);
        assertThat(historyChargeAmountCapture.getValue()).isEqualTo(expectedChargeAmount);
        assertThat(historyTransactionTypeCapture.getValue()).isEqualTo(expectedTransactionType);
    }
}