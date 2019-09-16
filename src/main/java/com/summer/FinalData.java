package com.summer;

import java.util.Random;

public class FinalData {
	private static Random rand = new Random(47);
	private String id;

	public FinalData(String id) {
		this.id = id;
	}

	private final int valueOne = 9;
	private static final int VALUE_TWO = 99;
	public static final int VALUE_THREE = 39;
	private final int i4 = rand.nextInt(20);
	static final int INT_5 = rand.nextInt(20);
	private Value v1 = new Value(11);
	private final Value v2 = new Value(22);
	private static final Value VAL_3 = new Value(33);

	private final int[] a = {1, 2, 3, 4, 5, 6};
	@Override
	public String toString() {
		return id + ":" + "i4=" + i4 + " INT_5=" + INT_5;
	}

	public static void main(String[] args) {
		FinalData fd = new FinalData("fd1");
		System.out.println(fd.v2);
		fd.v2.i++;
		System.out.println(fd.v2);
		System.out.println(fd.i4);
		System.out.println("INT_5=" + fd.INT_5);
		System.out.println(fd.VAL_3);
		FinalData.VAL_3.i++;
		System.out.println(fd.VAL_3);


		fd.v1 = new Value(9);
		System.out.println(fd.v1);
		for (int i = 0; i < fd.a.length; i++) {
			fd.a[i]++;
			System.out.println(fd.a[i]);
		}
		System.out.println(fd);
		System.out.println("createing new FinalData");
		FinalData fd2 = new FinalData("fd2");
		System.out.println(fd);
		System.out.println(fd2);

		FinalData fd3 = new FinalData("fd3");
		System.out.println(fd3);
	}
}

class Value {
	public static int VALUE_THREE;

	int i ;

	public Value(int i) {
		this.i = i;
	}

	@Override
	public String toString() {
		return Integer.toString(i);
	}
}
