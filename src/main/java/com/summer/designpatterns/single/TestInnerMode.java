package com.summer.designpatterns.single;

public class TestInnerMode {
	public static void main(String[] args) {
		//��̬�ڲ���ģʽ
		new Thread(){
			@Override
			public void run() {
				System.out.println(InnerClassMode.getInstance());;
			}
		}.start();

		new Thread(){
			@Override
			public void run() {
				System.out.println(InnerClassMode.getInstance());;
			}
		}.start();

	}
}
