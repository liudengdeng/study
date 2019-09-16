package com.summer.designpatterns.factorymethod;

import com.summer.designpatterns.BenTruck;
import com.summer.designpatterns.Car;

/**
 * �������ۿ����Ĺ���
 * @author liu_dd
 * @date 2018/12/30 19:27
 * @version 1.0.0
 */
public class BenTruckFactory implements CarMethodFactory {

	public Car createCar() {
		return new BenTruck();
	}
}
