package kr.hhplus.be.server.config.redis;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public interface LockTemplate {

    <T> T simpleLock(String key, Supplier<T> supplier);

    <T> T spinLock(String key, Supplier<T> supplier) throws InterruptedException;

    <T> T pubSubLock(String key, long waitTime, long releaseTime, TimeUnit timeUnit, Supplier<T> supplier);

    <T> T executeWithLocks(List<LockCommand> locks, Supplier<T> supplier);
}
