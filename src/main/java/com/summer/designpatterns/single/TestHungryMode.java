package com.summer.designpatterns.single;

public class TestHungryMode {
	public static void main(String[] args) {
		//¶öººÊ½
		new Thread(){
			@Override
			public void run() {
				System.out.println(HungryMode.getInstance());;
			}
		}.start();

		new Thread(){
			@Override
			public void run() {
				System.out.println(HungryMode.getInstance());;
			}
		}.start();
	}
}
