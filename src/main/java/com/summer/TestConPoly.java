package com.summer;

/**
 * �˳����ܽ᣺
 * �ڹ�������Ψһ�ܹ���ȫ���õķ���ʱ�����е�final����private��������Щ�������ᱻ���า��
 * @author: liu_dd
 * @date: 2018/12/14 20:47
 * @Version: 1.0.0
 */
public class TestConPoly {
	public static void main(String[] args) {
		new TestConB(5,"ldd");
	}
}

class TestConA {
	private void draw() {
		System.out.println("TestA.draw");
	}

	TestConA() {
		System.out.println("TestA.draw before");
		this.draw();//������д��draw���������¸����ʼ�����õ��������draw����,��ʼ�������ʱ���ȳ�ʼ�����࣬��ʱ����ĳ�Ա������δ��ʼ�������������draww������ӡ�������Ա����Ϊ��ʼֵ
		System.out.println("TestA.draw after");
	}
}

class TestConB extends TestConA {
	private int radius = 1;
	private String bb = "testb";
//	@Override
	void draw() {
		System.out.println("TestB.draw radius=" + radius + "bb=" + bb);
	}

	TestConB(int i,String j) {
		radius = i;
		bb = j;
		System.out.println("TestB.TestConB radius=" + radius + "bb=" + bb);
	}
}











