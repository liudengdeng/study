package com.summer.designpatterns.factorymethod;

import com.summer.designpatterns.Car;
import com.summer.designpatterns.BmwTruck;

/**
 * 创建宝马卡车的工厂
 * @author liu_dd
 * @date 2018/12/30 19:33
 * @version 1.0.0
 */
public class BmwTruckFactory implements CarMethodFactory {
	public Car createCar() {
		return new BmwTruck();
	}
}
