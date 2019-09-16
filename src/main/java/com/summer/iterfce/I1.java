package com.summer.iterfce;

import sun.nio.cs.ext.IBM300;

public interface I1 {
	void f();
}

interface I2 {
	int f(int i);
}

interface I3 {
	int f();
}

class C {
	public int f() {
		return 1;
	}
}

class C2 implements I1, I2 {

	public void f() {

	}

	public int f(int i) {
		return 1;
	}
}

class C3 extends C implements I2 {

	public int f(int i) {
		return 1;
	}
}

class C4 extends C implements I3 {
	@Override
	public int f() {
		return 1;
	}
}

//会编译报错，因为C类中和I1接口中的方法相同，只是返回类型不同(重载)
//class C5 extends C implements I1 {
//
//}

//会编译报错，I1接口和I3接口中有相同方法
//interface I4 extends I1, I3 {
//	void f();
//}
