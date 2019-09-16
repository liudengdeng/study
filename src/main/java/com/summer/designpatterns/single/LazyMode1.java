package com.summer.designpatterns.single;

/**
 * 懒汉式单例
 * 线程安全
 * @author liu_dd
 * @date 2019/1/1 15:20
 * @version 1.0.0
 */
public class LazyMode1 {
	private LazyMode1() {

	}

	private static LazyMode1 lazyMode = null;
	private static volatile LazyMode1 lazyMode2 = null;

	public static synchronized LazyMode1 getInstance() {
		if (lazyMode == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lazyMode = new LazyMode1();
		}
		return lazyMode;
	}

	//public static synchronized LazyMode1 getInstance()跟这种方式一样，线程安全
	public static LazyMode1 getInstance1() {
		synchronized (LazyMode1.class) {
			if (lazyMode == null) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				lazyMode = new LazyMode1();
			}
			return lazyMode;
		}
	}

	//线程不安全
	public static LazyMode1 getInstance2() {
			if (lazyMode == null) {
				synchronized (LazyMode1.class) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					lazyMode = new LazyMode1();
				}
			}
			return lazyMode;
	}

	/**
	 * 线程安全,双重锁机制看起来没有问题，因为创建一个新的对象不是原子性操作。
	 * 1.分配内存
	 * 2.初始化构造器
	 * 3.将对象指向分配的内存地址
	 * 由于jvm调优可能会将3和2操作反向，先把对象地址赋值给对象了，还未初始化构造器的时候假如后续的线程请求的getInstance，会判断lazyMode为null
	 */
	public static LazyMode1 getInstance3() {
		if (lazyMode == null) {
			synchronized (LazyMode1.class) {
				if (lazyMode == null) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					lazyMode = new LazyMode1();
				}
			}
		}
		return lazyMode;
	}

	/**
	 * 该方式优化了getInstance3可能线程不安全的情况
	 * lazyMode2实例属性前面加上volatile，
	 * 禁止了JVM自动的指令重排序优化，强行保证线程中对变量所做的任何写入操作对其他线程都是即时可见的，
	 */
	public static LazyMode1 getInstance4() {
		if (lazyMode == null) {
			synchronized (LazyMode1.class) {
				if (lazyMode == null) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					lazyMode2 = new LazyMode1();
				}
			}
		}
		return lazyMode;
	}
}
