package com.summer;

public class Testq {
	private int[] item;
	private int next = 0;


	public static void main(String[] args) {
		Testq tq = new Testq();
		tq.item = new int[10];
		for (int i = 0; i < tq.item.length; i++) {
//			tq.item[++tq.next] = i;
			tq.item[tq.next++] = i;
			System.out.println(tq.item[i]);
		}
	}
}
