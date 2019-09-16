package com.summer.designpatterns.buildpatterns;

/**
 * ����Ľ�����    
 * @author liu_dd
 * @date 2019/1/5 11:12
 * @version 1.0.0
 */
public class BigEyesBeautyBuilder implements GeneBuilder {

	private Beauty beauty;

	public void createHair() {
		beauty.setHair("�ڳ�ֱ");
	}

	public void createEye() {
		beauty.setEye("���۾�");
	}

	public void createMouth() {
		beauty.setMouth("С��");
	}

	public void createNose() {
		beauty.setNose("�α�");
	}

	public void createFigure() {
		beauty.setFigure("����");
	}

	public void createBeauty() {
		beauty = new Beauty();
	}

	public Beauty getBeauty() {
		return beauty;
	}
}
