package com.summer.innerclass;

public class DoThis {
	void f() {
		System.out.println("DoThis.f()");
	}

	public class Inner {
		public DoThis outer() {
			return DoThis.this;
			//编译报错this代表的是Inner类对象
			//			return this;
		}
	}

	public Inner inner() {
		return new Inner();
	}

	public static void main(String[] args) {
		DoThis dt = new DoThis();
//		Inner in = dt.inner();
		//必须通过外部类对象引用.new 直接  Inner in = new Inner();  会编译报错,除非Inner类为public static静态内部类
		Inner in = dt.new Inner();
		in.outer().f();
	}
}
