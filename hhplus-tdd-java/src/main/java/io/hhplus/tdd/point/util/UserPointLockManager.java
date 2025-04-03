package io.hhplus.tdd.point.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class UserPointLockManager {

    private final ConcurrentHashMap<Long, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    public void lock(long userId) {
        lockMap.computeIfAbsent(userId, (id) -> new ReentrantLock())
                .lock();
    }

    public void release(long userId) {
        lockMap.get(userId)
                .unlock();
    }
}
