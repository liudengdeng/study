package com.summer.designpatterns.simplefactory;

import com.summer.designpatterns.BenTruck;
import com.summer.designpatterns.Car;
import com.summer.designpatterns.BmwTruck;

/**
 * ���Դ������ֳ��Ĺ���
 * @author liu_dd
 * @date 2018/12/30 19:24
 * @version 1.0.0
 */
public class CarFactory {
	public static Car createCar(String carName) {
		if (carName == null) {
			return null;
		}
		if ("bmw".equals(carName)) {
			return new BmwTruck();
		} else if ("ben".equals(carName)) {
			return new BenTruck();
		} else {
			return null;
		}
	}
}
