package com.summer.designpatterns.buildpatterns;

public class TestDirector {
	public static void main(String[] args) {
		GeneBuilder geneBuilder = new BigEyesBeautyBuilder();
		//1.构造方法
		Director director = new Director(geneBuilder);
		System.out.println(director.getBeauty());

		//2.set方法
		Director director1 = new Director();
		director1.setGeneBuilder(geneBuilder);
		System.out.println(director1.getBeauty());

		//3.形参传输
		Director director2 = new Director();
		System.out.println(director2.getBeauty2(geneBuilder));
	}
}
