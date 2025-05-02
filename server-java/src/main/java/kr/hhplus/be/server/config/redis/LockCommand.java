package kr.hhplus.be.server.config.redis;

import java.util.concurrent.TimeUnit;

public record LockCommand(
        String key,
        LockMethod method,
        Long waitTime,
        Long leaseTime,
        TimeUnit timeUnit
) {
    public LockCommand(String key, LockMethod method, Long waitTime, Long leaseTime, TimeUnit timeUnit) {
        this.key = key;
        this.method = method;
        this.waitTime = waitTime;
        this.leaseTime = leaseTime;
        this.timeUnit = timeUnit;
    }

    public LockCommand(String key, LockMethod method) {
        this(key, method,  null, null, null);
        if (method == null || method.equals(LockMethod.PUBSUB)) {
            throw new IllegalArgumentException("PUBSUB 방식은 waitTime, leaseTime, timeUnit을 설정해야 합니다.");
        }
    }
}
