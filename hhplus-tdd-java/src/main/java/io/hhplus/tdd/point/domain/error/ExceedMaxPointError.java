package io.hhplus.tdd.point.domain.error;

import io.hhplus.tdd.DomainException;
import io.hhplus.tdd.ErrorResponse;

public class ExceedMaxPointError extends DomainException {
    public ExceedMaxPointError() {
        super(new ErrorResponse("400", "충전 가능한 포인트를 초과하였습니다."));
    }
}
