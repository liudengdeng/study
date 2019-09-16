package com.summer.designpatterns.buildpatterns;

/**
 * 具体的建造者    
 * @author liu_dd
 * @date 2019/1/5 11:12
 * @version 1.0.0
 */
public class BigEyesBeautyBuilder implements GeneBuilder {

	private Beauty beauty;

	public void createHair() {
		beauty.setHair("黑长直");
	}

	public void createEye() {
		beauty.setEye("大眼睛");
	}

	public void createMouth() {
		beauty.setMouth("小嘴");
	}

	public void createNose() {
		beauty.setNose("俏鼻");
	}

	public void createFigure() {
		beauty.setFigure("丰满");
	}

	public void createBeauty() {
		beauty = new Beauty();
	}

	public Beauty getBeauty() {
		return beauty;
	}
}
