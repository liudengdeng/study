package com.summer.designpatterns.buildpatterns;

public class TestDirector {
	public static void main(String[] args) {
		GeneBuilder geneBuilder = new BigEyesBeautyBuilder();
		//1.���췽��
		Director director = new Director(geneBuilder);
		System.out.println(director.getBeauty());

		//2.set����
		Director director1 = new Director();
		director1.setGeneBuilder(geneBuilder);
		System.out.println(director1.getBeauty());

		//3.�βδ���
		Director director2 = new Director();
		System.out.println(director2.getBeauty2(geneBuilder));
	}
}
