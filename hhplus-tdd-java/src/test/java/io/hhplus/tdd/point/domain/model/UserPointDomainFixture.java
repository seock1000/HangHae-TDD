package io.hhplus.tdd.point.domain.model;

import io.hhplus.tdd.point.domain.UserPointDomain;
import io.hhplus.tdd.point.domain.PointConstant;

public class UserPointDomainFixture {

    public static UserPointDomain initByZero() {
        return new UserPointDomain(1, 0, System.currentTimeMillis());
    }

    public static UserPointDomain initByMin() {
        return new UserPointDomain(1, PointConstant.MIN_POINT, System.currentTimeMillis());
    }

    public static UserPointDomain initByMax() {
        return new UserPointDomain(1, PointConstant.MAX_POINT, System.currentTimeMillis());
    }
}
