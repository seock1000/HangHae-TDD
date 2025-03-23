package io.hhplus.tdd.point.domain.error;

import io.hhplus.tdd.DomainException;
import io.hhplus.tdd.ErrorResponse;

public class UnderMinPointError extends DomainException {
    public UnderMinPointError() {
        super(new ErrorResponse("400", "최소 포인트 이상 사용할 수 없습니다."));
    }
}
