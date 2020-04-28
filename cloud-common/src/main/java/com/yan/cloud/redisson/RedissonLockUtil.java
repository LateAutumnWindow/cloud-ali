package com.yan.cloud.redisson;

import org.redisson.RedissonMultiLock;
import org.redisson.api.*;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class RedissonLockUtil {

    private static RedissonClient redissonClient;

    public void setRedissonClient(RedissonClient locker) {
        redissonClient = locker;
    }

    /**
     * 加锁
     * @param lockKey
     * @return
     */
    public static RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * 释放锁
     * @param lockKey
     */
    public static void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

    /**
     * 释放锁
     * @param lock
     */
    public static void unlock(RLock lock) {
        lock.unlock();
    }

    /**
     * 带超时的锁
     * @param lockKey
     * @param timeout 超时时间   单位：秒
     */
    public static RLock lock(String lockKey, int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, TimeUnit.SECONDS);
        return lock;
    }

    /**
     * 带超时的锁
     * @param lockKey
     * @param unit 时间单位
     * @param timeout 超时时间
     */
    public static RLock lock(String lockKey, TimeUnit unit , int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }

    /**
     * 尝试获取锁
     * @param lockKey
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public static boolean tryLock(String lockKey, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 尝试获取锁
     * @param lockKey
     * @param unit 时间单位
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public static boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 公平锁：排队等待
     * @param lock
     * @param wait 等待锁得时间，超时获取不到锁自动解开
     * @return
     */
    public static RLock getFairLock(String lock, Long wait, TimeUnit tu) {
        RLock fairLock = redissonClient.getFairLock(lock);
        fairLock.lock(wait, tu);
        return  fairLock;
    }

    /**
     * 联锁：多个key同时上锁成功才能获取到锁
     * @param locks
     * @return
     */
    public static RLock getMultiLock(String... locks) {
        CopyOnWriteArrayList<RLock> list = new CopyOnWriteArrayList<>();
        for (String lock : locks) {
            RLock mlock = redissonClient.getLock(lock);
            list.add(mlock);
        }
        RLock[] rl = new RLock[list.size()];
        RLock[] rLocks = list.toArray(rl);
        return new RedissonMultiLock(rLocks);
    }

    /**
     * 读写锁： 允许同时有多个读锁和一个写锁处于加锁状态。
     * @param key
     * @return
     */
    public static RLock getReadWriteLockRead(String key) {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(key);
        return readWriteLock.readLock();
    }
    public static RLock getReadWriteLockWrite(String key) {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(key);
        return readWriteLock.writeLock();
    }

    /**
     * 信号量锁
     * @param key
     * @return
     */
    public static RSemaphore getSemaphoreLock(String key, int permits) {
        RSemaphore semaphore = redissonClient.getSemaphore(key);
        semaphore.trySetPermits(permits);
        return semaphore;
    }

    /**
     * 闭锁
     * @param key
     * @param num 等待线程得数量
     * @return
     */
    public static RCountDownLatch getRCountDownLatch(String key, long num) {
        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch(key);
        countDownLatch.trySetCount(num);
        return countDownLatch;
    }
}
