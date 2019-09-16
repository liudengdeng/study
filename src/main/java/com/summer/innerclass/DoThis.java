package com.summer.innerclass;

public class DoThis {
	void f() {
		System.out.println("DoThis.f()");
	}

	public class Inner {
		public DoThis outer() {
			return DoThis.this;
			//���뱨��this�������Inner�����
			//			return this;
		}
	}

	public Inner inner() {
		return new Inner();
	}

	public static void main(String[] args) {
		DoThis dt = new DoThis();
//		Inner in = dt.inner();
		//����ͨ���ⲿ���������.new ֱ��  Inner in = new Inner();  ����뱨��,����Inner��Ϊpublic static��̬�ڲ���
		Inner in = dt.new Inner();
		in.outer().f();
	}
}
