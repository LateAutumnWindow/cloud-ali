package com.yan.cloud.config;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


class redisson {
    static ThreadPoolExecutor tpe = new ThreadPoolExecutor(
                    5,
                    8,
                    5,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(3),
                    Executors.defaultThreadFactory(),
                    new ThreadPoolExecutor.AbortPolicy());


    // volatile
    volatile static Integer num = 0;
    volatile static Integer xse = 0;
    volatile static Integer a = 0;
    volatile static Integer b = 0;
    // Atomic
    // 反例
    private Integer sec = 0;
    public void secAdd() {this.sec += 1;}
    public Integer getSec() {return  this.sec;}
    // 正例
    private AtomicInteger see = new AtomicInteger(0);
    public void seeAdd() {see.getAndIncrement();}
    public Integer getSee() {return  see.get();}
    // wait   notify   notifyAll
    static class Uel{
        public synchronized void before() {
            System.out.println("wait  ----- start");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("wait  ----- end");
        }
        public synchronized void after() {
            System.out.println("notify  ----- start");
            notifyAll();
            System.out.println("notify  ----- end");
        }
    }

    public static String jj(int v){
        if (v == 1) {
            return "1";
        } else {
            if (v % 2 == 0) {
               return jj( v/2 ) + "0";
            } else {
               return jj(v/2) + "1";
            }
        }
    }


    public static void main(String[] args) throws Exception {
        int n = 23 - 1;
        n |= n >>> 1;
        System.out.println(n);


        // ---------------------- ReentrantReadWriteLock  ------------------------------
//        List<Integer> list = new ArrayList<>();
//        for (int i = 0; i < 200; i++) {
//            list.add(i);
//        }
//        ReentrantReadWriteLock rl = new ReentrantReadWriteLock();
//        // 读
//        tpe.execute(() -> {
//            rl.readLock().lock();
//            for (Integer integer : list) {
//                System.out.println(Thread.currentThread().getName() + " ---  " + integer);
//            }
//            rl.readLock().unlock();
//        });
//        tpe.execute(() -> {
//            rl.readLock().lock();
//            for (Integer integer : list) {
//                System.out.println(Thread.currentThread().getName() + " ---  " + integer);
//            }
//            rl.readLock().unlock();
//        });
////        // 写
//        tpe.execute(() -> {
//            rl.writeLock().lock();
//            for (int i = 0; i < 20; i++) {
//                list.add(i);
//                System.out.println(Thread.currentThread().getName() + " --writer--  " + i);
//            }
//            rl.writeLock().unlock();
//        });
//        tpe.execute(() -> {
//            rl.writeLock().lock();
//            for (int i = 0; i < 20; i++) {
//                list.add(i);
//                System.out.println(Thread.currentThread().getName() + " --writer--  " + i);
//            }
//            rl.writeLock().unlock();
//        });
//        for (Integer integer : list) {
//            System.out.println(integer);
//        }
        // ---------------------- ReentrantReadWriteLock  ------------------------------



        // ---------------------- await() signal() signalAll() ------------------------------
//        ReentrantLock rl = new ReentrantLock();
//        Condition condition = rl.newCondition();
//        tpe.execute(() -> {
//            try{
//                rl.lock();
//                System.out.println("await --- start");
//                condition.await();
//                System.out.println("await --- end");
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                rl.unlock();
//            }
//        });
//        tpe.execute(() -> {
//            try{
//                rl.lock();
//                System.out.println("signal --- start");
//                condition.signal();
//                System.out.println("signal --- end");
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                rl.unlock();
//            }
//        });
        // ---------------------- await() signal() signalAll() ------------------------------


        // ---------------------- Object.wait() Object.notify() Object.notifyAll()------------------------------
        // wait notify 要用在 sync 修饰的方法，代码块中 否则会在运行时抛出 IllegalMonitorStateException。
//        Uel el = new Uel();
//        tpe.execute(() -> {el.before();});
//        tpe.execute(() -> {el.after();});
        // ---------------------- Object.wait() Object.notify() Object.notifyAll()------------------------------


        // ---------------------- Thread.join() ------------------------------
//        Thread a = new Thread(() -> {
//            for (int i = 0; i < 10; i++) {
//                try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
//                System.out.println(i + "  ---  ");
//            }
//        }, "A");
//        a.start();
////        a.join();  // 如果子线程使用了join主线程会挂起子线程执行完后再次执行
//        System.out.println("Main");
        // ---------------------- Thread.join() ------------------------------

        // ---------------------- AtomicStampedReference ------------------------------
//        AtomicStampedReference<String> asr = new AtomicStampedReference<>("A", 0);
//        AtomicReference<String> ar = new AtomicReference<>("A");
//        new Thread(() -> {
//            ar.compareAndSet("A", "B");
//            System.out.println(Thread.currentThread().getName() + "   -----   " +ar.get());
//        }, "T1").start();
//        new Thread(() -> {
//            ar.compareAndSet("B", "A");
//            System.out.println(Thread.currentThread().getName() + "   -----   " +ar.get());
//        }, "T3").start();
//        new Thread(() -> {
//            ar.compareAndSet("A", "B");
//            System.out.println(Thread.currentThread().getName() + "   -----   " +ar.get());
//        }, "T2").start();
        // ---------------------- AtomicStampedReference ------------------------------




        // ---------------------- Semaphore ------------------------------
//        Semaphore sem = new Semaphore(5);
//        for (int i = 0; i < 8; i++) {
//            tpe.execute(() -> {
//                try {
//                    // 获取信号量
//                    sem.acquire();
//                    try {TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e) {e.printStackTrace();}
//                    // 释放信号
//                    System.out.println(Thread.currentThread().getName() + " 释放信号");
//                    sem.release();
//                } catch (InterruptedException  e) {
//                    e.printStackTrace();
//                }
//            });
//        }
        // ---------------------- Semaphore ------------------------------


        // ---------------------- CyclicBarrier ------------------------------
//        CyclicBarrier cb = new CyclicBarrier(5);
//        for (int i = 0; i < 5; i++) {
//            tpe.execute(() -> {
//                int q = new Random().nextInt(5);
//                System.out.println(q);
//                try {TimeUnit.SECONDS.sleep(q);} catch (InterruptedException e) {e.printStackTrace();}
//                System.out.println(Thread.currentThread().getName() + "等待！！！");
//                try {
//                    cb.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (BrokenBarrierException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(Thread.currentThread().getName() + "结束等待！！！");
//            });
//        }
        // ---------------------- CyclicBarrier ------------------------------

        // ---------------------- CountDownLatch ------------------------------
//        CountDownLatch cdl = new CountDownLatch(5);
//        for (int i = 0; i < 5; i++) {
//            tpe.execute(() -> {
//                for (int j = 0; j < 5; j++) {
//                    System.out.println(Thread.currentThread().getName() + "  --  " + j);
//                }
//                System.out.println(Thread.currentThread().getName() + "等待！！！");
//                cdl.countDown();
//            });
//        }
//        System.out.println("fffff");
//        cdl.await();
//        System.out.println("ddddd");
        // ---------------------- CountDownLatch ------------------------------


        // ---------------------- AtoMic ------------------------------
//        redisson red = new redisson();
        // 反例
//        for (int i = 0; i < 5; i++) {
//            tpe.execute(() -> {
//                for (int j = 0; j < 10; j++) {
//                    red.secAdd();
//                }
//            });
//        }
//        System.out.println(red.getSec());
        // 正例
//        for (int i = 0; i < 5; i++) {
//            tpe.execute(() -> {
//                for (int j = 0; j < 10; j++) {
//                    red.seeAdd();
//                }
//            });
//        }
//        try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
//        System.out.println(red.getSee());

        // ---------------------- volatile ------------------------------


        // ---------------------- volatile ------------------------------
//       Thread at = new Thread(() -> {
//            try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
//            a = 1;
//            xse = b;
//        }, "A");
//        Thread bt = new Thread(() -> {
//            try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
//            b = 1;
//            num = a;
//        }, "B");
//        at.start();
//        bt.start();
//        at.join();
//        bt.join();
//        System.out.println(num + xse);
        // ---------------------- volatile ------------------------------


        // ---------------------- ArrayList 线程不安全 ------------------------------
        // 不安全
//        List<Integer> list = new ArrayList<>();
//        new Thread(() -> {
//            for (int i = 0; i < 1000; i++) {
//                list.add(i);
//                try {TimeUnit.MILLISECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
//            }
//        }, "A").start();
//        new Thread(() -> {
//            for (int i = 1000; i < 2000; i++) {
//                list.add(i);
//                try {TimeUnit.MILLISECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
//            }
//        }, "B").start();
//        try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
//        for (Integer integer : list) {
//            if (integer != null) continue;
//            System.out.println(integer);
//        }
        // 安全
//        CopyOnWriteArrayList<Integer> cow = new CopyOnWriteArrayList<>();
//        new Thread(() -> {
//            for (int i = 0; i < 1000; i++) {
//                cow.add(i);
//                try {TimeUnit.MILLISECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
//            }
//        }, "A").start();
//        new Thread(() -> {
//            for (int i = 1000; i < 2000; i++) {
//                cow.add(i);
//                try {TimeUnit.MILLISECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
//            }
//        }, "B").start();
//        try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
//        for (Integer integer : cow) {
//            if (integer != null) continue;
//            System.out.println(integer);
//        }
        // ---------------------- ArrayList 线程不安全 ------------------------------
        // 释放线程池
        tpe.shutdown();
    }
}

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