package com.summer.designpatterns.single;

/**
 * 饿汉式单例
 * 本身就是线程安全的，类编译时就初始化好了
 * 虽然是线程安全的，但是如果我们访问了该类的其他静态资源，就导致实例的初始化，实际上后面又用不上该实例，就会造成内存浪费，一般不用该模式
 * @author liu_dd
 * @date 2019/1/1 15:02
 * @version 1.0.0
 */
public class HungryMode {
	private HungryMode() {
		System.out.println("hungryMode...");
	}

	private static HungryMode hungryMode = new HungryMode();

	public static HungryMode getInstance() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return hungryMode;
	}
}
