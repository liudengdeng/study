package com.summer.designpatterns.factorymethod;

import com.summer.designpatterns.Car;
import com.summer.designpatterns.BmwTruck;

/**
 * �����������Ĺ���
 * @author liu_dd
 * @date 2018/12/30 19:33
 * @version 1.0.0
 */
public class BmwTruckFactory implements CarMethodFactory {
	public Car createCar() {
		return new BmwTruck();
	}
}
