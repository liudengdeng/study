package com.summer.designpatterns.buildpatterns;

/**
 * 具体的指挥者    
 * @author liu_dd
 * @date 2019/1/5 11:12
 * @version 1.0.0
 */
public class Director {
	private GeneBuilder geneBuilder;

	public Director() {

	}
	//构造方法初始化建造者
	public Director(GeneBuilder geneBuilder) {
		this.geneBuilder = geneBuilder;
	}
	//set方法初始化建造者
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

	//直接先初始化在调用方法
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
