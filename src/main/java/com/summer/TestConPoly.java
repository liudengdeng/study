package com.summer;

/**
 * 此程序总结：
 * 在构造器内唯一能够安全调用的方法时父类中的final或者private方法，这些方法不会被子类覆盖
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
		this.draw();//子类重写了draw方法，导致父类初始化调用的是子类的draw方法,初始化子类的时候先初始化父类，此时子类的成员变量还未初始化，导致子类的draww方法打印的子类成员变量为初始值
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











