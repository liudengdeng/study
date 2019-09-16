package com.summer.designpatterns.buildpatterns;

/**
 * �����ָ����    
 * @author liu_dd
 * @date 2019/1/5 11:12
 * @version 1.0.0
 */
public class Director {
	private GeneBuilder geneBuilder;

	public Director() {

	}
	//���췽����ʼ��������
	public Director(GeneBuilder geneBuilder) {
		this.geneBuilder = geneBuilder;
	}
	//set������ʼ��������
	public void setGeneBuilder(GeneBuilder geneBuilder) {
		this.geneBuilder = geneBuilder;
	}

	public Beauty getBeauty() {
		geneBuilder.createBeauty();
		geneBuilder.createEye();
		geneBuilder.createFigure();
		geneBuilder.createHair();
		geneBuilder.createMouth();
		geneBuilder.createNose();
		return geneBuilder.getBeauty();
	}

	//ֱ���ȳ�ʼ���ڵ��÷���
	public Beauty getBeauty2(GeneBuilder geneBuilder) {
		geneBuilder.createBeauty();
		geneBuilder.createEye();
		geneBuilder.createFigure();
		geneBuilder.createHair();
		geneBuilder.createMouth();
		geneBuilder.createNose();
		return geneBuilder.getBeauty();
	}
}
