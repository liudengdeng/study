package com.summer.designpatterns.single;

/**
 * 懒汉式单例
 * 线程不安全
 * @author liu_dd
 * @date 2019/1/1 15:20
 * @version 1.0.0
 */
public class LazyMode {
	private LazyMode() {

	}

	private static LazyMode lazyMode = null;

	public static LazyMode getInstance() {
		if (lazyMode == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lazyMode = new LazyMode();
		}
		return lazyMode;
	}
}
