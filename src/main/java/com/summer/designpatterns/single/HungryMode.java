package com.summer.designpatterns.single;

/**
 * ����ʽ����
 * ��������̰߳�ȫ�ģ������ʱ�ͳ�ʼ������
 * ��Ȼ���̰߳�ȫ�ģ�����������Ƿ����˸����������̬��Դ���͵���ʵ���ĳ�ʼ����ʵ���Ϻ������ò��ϸ�ʵ�����ͻ�����ڴ��˷ѣ�һ�㲻�ø�ģʽ
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
