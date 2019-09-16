package com.summer;

import java.util.Random;

public class TestRandom {
	private static Random rand = new Random(47);
	private static Random rand2 = new Random();
	int a = rand.nextInt(20);
	final int b = rand.nextInt(20);
	static final int c = rand.nextInt(20);
	public static void main(String[] args) {
		TestRandom tr1 = new TestRandom();
		TestRandom tr2 = new TestRandom();
		TestRandom tr3 = new TestRandom();
		System.out.println(tr1.a+" "+tr1.b+" "+tr1.c);
		System.out.println(tr2.a+" "+tr2.b+" "+tr2.c);
		System.out.println(tr3.a+" "+tr3.b+" "+tr3.c);
		System.out.println(rand.nextInt(20));
		System.out.println(rand.nextInt(20));
//		System.out.println(rand2.nextInt(20));
//		System.out.println(rand2.nextInt(20));

		Random r = new Random(47);
		int a = r.nextInt(26);
		System.out.println(a);
		System.out.println(r.nextInt(26));
		System.out.println(r.nextInt(26));
	}
}
