package com.summer.designpatterns.buildpatterns;

/**
 * ������콨����
 * @author liu_dd
 * @date 2019/1/5 11:01
 * @version 1.0.0
 */
public interface GeneBuilder {

	void createHair();

	void createEye();

	void createMouth();

	void createNose();

	void createFigure();

	void createBeauty();

	Beauty getBeauty();
}
