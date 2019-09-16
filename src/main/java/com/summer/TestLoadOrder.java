package com.summer;

public class TestLoadOrder {
	public static void main(String[] args) {
		TestB testB  = new TestB();
	}
}

class TestA {
	private static int a1 = testA("a1");
	private int a2 = testA("a2");
	TestA() {
		System.out.println("a init");
	}
	static int testA(String str) {
		System.out.println(str);
		return 1;
	}

	int testA2() {
		System.out.println("aaaa2");
		return 2;
	}
}

class TestB extends TestA{

	private static int b3 = testA("b3");
	private int b = TestA.testA("b");
	private int b2 = this.testA2();
	TestB() {
		System.out.println("b init");
	}
}
