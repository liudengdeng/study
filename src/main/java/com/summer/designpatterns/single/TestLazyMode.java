package com.summer.designpatterns.single;

public class TestLazyMode {
	public static void main(String[] args) {
		//����ʽ
		new Thread(){
			@Override
			public void run() {
				System.out.println(LazyMode.getInstance());;
			}
		}.start();

		new Thread(){
			@Override
			public void run() {
				System.out.println(LazyMode.getInstance());;
			}
		}.start();
	}
}
