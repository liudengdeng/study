package com.summer;

public class TestExtends {
	protected void sayHehe() {
		System.out.println("hehe");
	}

	public void sayHello() {
		System.out.println("hello");
	}
	TestExtends() {
		System.out.println("testStatic");
	}
}
class a extends TestExtends{
	a() {
		System.out.println("a");
	}
}
class b extends a {
	b() {
		System.out.println("b");
	}
}
class CTest extends b {
	CTest() {
		System.out.println("c");
	}

	public static void main(String[] args) {
		CTest e = new CTest();
		e.sayHello();
	}
}
