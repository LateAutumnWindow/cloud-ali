package com.yan.cloud.config;


import cn.hutool.core.thread.SemaphoreRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;


class Jm {
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        Semaphore sem = new Semaphore(3);
        for (int i = 0; i < 12; i++) {
            new Thread(() -> {
                try {
                    sem.acquire();
                    System.out.println("333333333333333333");
                    try{TimeUnit.SECONDS.sleep(2);}catch(InterruptedException e) {e.printStackTrace();}
                    sem.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "A").start();
        }

//        CyclicBarrier cb = new CyclicBarrier(2);
//        new Thread(() -> {
//            System.out.println(Thread.currentThread().getName()+ "  0000000000000000");
//            try {
//                cb.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (BrokenBarrierException e) {
//                e.printStackTrace();
//            }
//        }, "A").start();
//        try{TimeUnit.SECONDS.sleep(1);}catch(InterruptedException e) {e.printStackTrace();}
//        System.out.println(cb.getNumberWaiting() + "EEEEE");
//        try{TimeUnit.SECONDS.sleep(2);}catch(InterruptedException e) {e.printStackTrace();}
//        new Thread(() -> {
//            System.out.println(Thread.currentThread().getName()+ "  0000000000000000");
//            try {
//                cb.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (BrokenBarrierException e) {
//                e.printStackTrace();
//            }
//        }, "B").start();
//        System.out.println("Main  go" );


//        CountDownLatch cdl = new CountDownLatch(2);
//        new Thread(() -> {
//            cdl.countDown();
//            System.out.println(Thread.currentThread().getName()  + "-------");
//            try {
//                cdl.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }, "A").start();
//        try{TimeUnit.SECONDS.sleep(3);}catch(InterruptedException e) {e.printStackTrace();}
//        new Thread(() -> {
//            cdl.countDown();
//            System.out.println(Thread.currentThread().getName()  + "-------");
//            try {
//                cdl.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }, "B").start();
//        System.out.println("Main -------");
//        // WeakHashMap 当键失效的时候，在map中的值自动回收
//        WeakHashMap<Integer, String> whm = new WeakHashMap<>();
//        Integer a = new Integer(1);
//        Integer b = new Integer(2);
//        Integer c = new Integer(3);
//        whm.put(a, "A");
//        whm.put(b, "B");
//        whm.put(c, "C");
//        a = null;
//        b = null;
//        System.gc();
//        System.out.println(whm + "    " +whm.size());


//        // 实例: 软引用
//        Object o = new Object();
//        SoftReference<Object> objectSoftReference = new SoftReference<>(o);
//
//        System.out.println("eee------" +o);
//        System.out.println("www------" + objectSoftReference);
//        o = null;
//        try {
//            byte[] by = new byte[30 * 1024 * 1024];
//        } catch (Throwable t) {
//            t.printStackTrace();
//        } finally {
//            System.out.println("eee====" +o);
//            System.out.println("www====" + objectSoftReference.get());
//        }


//        KeyPair rsa = SecureUtil.generateKeyPair("RSA");
//        RSA r = SecureUtil.rsa(rsa.getPrivate().getEncoded(), rsa.getPublic().getEncoded());
//        byte[] aaas = r.encrypt("AAA", KeyType.PrivateKey);
//        System.out.println(new String(aaas));
//        byte[] encrypt = r.decrypt(aaas, KeyType.PublicKey);
//        System.out.println(new String(encrypt));
    }
}

class Poll{
    static AtomicStampedReference<Integer> je = new AtomicStampedReference<>(10, 1);

    public static void main(String[] args) {
        ThreadPoolExecutor exe = new ThreadPoolExecutor(
                3,
                5,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        exe.execute(() -> {
            je.compareAndSet(10, je.getReference() + 1, 1, je.getStamp() +1);
            System.out.println(je.getReference() + "   " + je.getStamp());
        });
        exe.shutdown();
    }
}

class JEE{
    int number = 0;
    public void add() {
        this.number = 60;
    }
}

class UYI{
    public static void main(String[] args) {
        JEE u = new JEE();
        new Thread(() -> {
            try{TimeUnit.SECONDS.sleep(3);}catch(InterruptedException e) {e.printStackTrace();}
            u.add();
            System.out.println("qqq");
        }, "A").start();
        System.out.println("eee");
        while (u.number == 0){}
    }
}

class Count{
    public static void main(String[] args) {
        test2();
    }

    public static void test2() {
        Semaphore sem = new Semaphore(3);
        for (int i = 0; i < 10; i++) {
            final int ti = i;
            new Thread(() -> {
                try {
                    sem.acquire();
                    System.out.println("ffff");
                    try{TimeUnit.SECONDS.sleep(2);}catch(InterruptedException e) {e.printStackTrace();}
                    sem.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, String.valueOf(ti)).start();
        }


    }

    void test1() {
        CountDownLatch cdl = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            final int ik = i;
            new Thread(() -> {
                System.out.println(ik + "       ffffff");
                cdl.countDown();
            }, String.valueOf(ik)).start();
        }
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("exit");
    }
}



class Cyc {
    public static void main(String[] args) {
        CyclicBarrier cb = new CyclicBarrier(5, () -> {
            System.out.println("js");
        });
        for (int i = 0; i < 5; i++) {
            final int ki = i;
            new Thread(() -> {
                System.out.println("线程执行:" + ki);
                try {
                    cb.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, String.valueOf(ki)).start();
        }
    }
}

public class SingleData {

    private static volatile SingleData singleData = null;

    private SingleData(){}

    public static SingleData getSingleData(){
        if (singleData == null) {
            synchronized(SingleData.class) {
                if(singleData == null){
                    singleData = new SingleData();
                }
            }
        }
        return singleData;
    }

}

class CountLatch{
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch cdl = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            final int ni = i;
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + ": " + String.valueOf(ni));
                cdl.countDown();
            }, String.valueOf(i)).start();
        }
        cdl.await(10, TimeUnit.SECONDS);
        System.out.println("执行完成");
    }
}

class Loock {
    public static void main(String[] args) {
        Loock lo = new Loock();
        for (int i = 0; i < 5; i++) {
            final int ki = i;
            new Thread(() -> {
                lo.put("key" + ki, ki);
            }, "t" + i).start();
        }

        for (int i = 0; i < 5; i++) {
            final int ki = i;
            new Thread(() -> {
                lo.get("key" + ki);
            }, "k" + i).start();
        }


    }

    private volatile Map<String, Object> map = new HashMap<>();
    ReentrantReadWriteLock rrw = new ReentrantReadWriteLock();

    public void put(String key, Object val) {
        rrw.writeLock().lock();
        try{
            System.out.println(Thread.currentThread().getName() + "：正在写入数据。。。");
            map.put(key, val);
            System.out.println(Thread.currentThread().getName() + "：写入数据完成");
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            rrw.writeLock().unlock();
        }
    }

    public void get(String key) {
        rrw.readLock().lock();
        try{
            System.out.println(Thread.currentThread().getName() + "：正在读取数据");
            System.out.println(Thread.currentThread().getName() + map.get(key));
            System.out.println(Thread.currentThread().getName() + "：读取完成");
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            rrw.readLock().unlock();
        }
    }
}