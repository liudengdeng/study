package com.summer;

public class TestPoly {
	public static void main(String[] args) {
		TestC testC = new TestD();
		System.out.println(testC.testA);
		System.out.println(testC.getTestA());
		System.out.println(testC.getTestA2());
	}
}

class TestC {
	public int testA = 0;

	public static String getTestA() {
		return "getTestA()";
	}

	public String getTestA2() {
		return "getTestA2()";
	}
}

class TestD extends TestC {
	public int testA = 1;

	public static String getTestA() {
		return "getTestAb";
	}
	@Override
	public String getTestA2() {
		return "getTestA2b";
	}
}
