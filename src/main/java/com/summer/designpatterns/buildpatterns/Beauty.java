package com.summer.designpatterns.buildpatterns;

/**
 * 具体的产品对象
 * @author liu_dd
 * @date 2019/1/5 10:57
 * @version 1.0.0
 */
public class Beauty {
	private String hair;
	private String eye;
	private String mouth;
	private String nose;
	private String figure;

	public String getHair() {
		return hair;
	}

	public void setHair(String hair) {
		this.hair = hair;
	}

	public String getEye() {
		return eye;
	}

	public void setEye(String eye) {
		this.eye = eye;
	}

	public String getMouth() {
		return mouth;
	}

	public void setMouth(String mouth) {
		this.mouth = mouth;
	}

	public String getNose() {
		return nose;
	}

	public void setNose(String nose) {
		this.nose = nose;
	}

	public String getFigure() {
		return figure;
	}

	public void setFigure(String figure) {
		this.figure = figure;
	}

	@Override
	public String toString() {
		return getHair() + getEye() + getMouth() + getNose() + getFigure();
	}
}
