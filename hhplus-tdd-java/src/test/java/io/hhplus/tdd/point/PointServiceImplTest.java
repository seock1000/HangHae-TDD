package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.format.annotation.NumberFormat;

import java.io.PipedInputStream;

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
     * 유저 포인트 조회 TC - 성공
     * parameter로 전달된 user id에 해당하는 UserPoint를 조회하는지 검증하기 위해 stubbing 사용
     */
    @Test
    @DisplayName("요청한 ID의 UserPoint를 읽어온다.")
    void readUserPoint() {
        //given
        UserPoint expectedUserPoint1 = new UserPoint(1, 10, System.currentTimeMillis());
        UserPoint expectedUserPoint2 = new UserPoint(2, 100, System.currentTimeMillis());
        UserPoint expectedUserPoint3 = new UserPoint(3, 1000, System.currentTimeMillis());

        Mockito.when(userPointTable.selectById(expectedUserPoint1.id()))
                .thenReturn(expectedUserPoint1);
        Mockito.when(userPointTable.selectById(expectedUserPoint2.id()))
                .thenReturn(expectedUserPoint2);
        Mockito.when(userPointTable.selectById(expectedUserPoint3.id()))
                .thenReturn(expectedUserPoint3);

        //when
        UserPoint actualUserPoint1 = pointService.readUserPoint(expectedUserPoint1.id());
        UserPoint actualUserPoint2 = pointService.readUserPoint(expectedUserPoint2.id());
        UserPoint actualUserPoint3 = pointService.readUserPoint(expectedUserPoint3.id());

        //then
        assertThat(expectedUserPoint1).isEqualTo(actualUserPoint1);
        assertThat(expectedUserPoint2).isEqualTo(actualUserPoint2);
        assertThat(expectedUserPoint3).isEqualTo(actualUserPoint3);
    }
}