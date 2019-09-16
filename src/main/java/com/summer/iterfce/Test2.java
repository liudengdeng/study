package com.summer.iterfce;

public class Test2 implements Test1{
	public void sayHi() {
		System.out.println("hi");
	}

	public static void main(String[] args) {
		Test1 t1 = new Test2();
		System.out.println(t1.aa);//可以访问的到，但是应该用Test1.aa访问
	}
}
