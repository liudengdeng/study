package com.summer.designpatterns;

import com.summer.designpatterns.abstractfactory.AbstractCarFactory;
import com.summer.designpatterns.abstractfactory.BenFactory;
import com.summer.designpatterns.abstractfactory.BwmFactory;
import com.summer.designpatterns.factorymethod.BenTruckFactory;
import com.summer.designpatterns.factorymethod.BmwTruckFactory;
import com.summer.designpatterns.factorymethod.CarMethodFactory;
import com.summer.designpatterns.simplefactory.CarFactory;

/**
 * 客户请求工厂生产车
 * @author liu_dd
 * @date 2018/12/30 19:17
 * @version 1.0.0
 */
public class Customer {
	public static void main(String[] args) {
		//1.简单工厂模式
		Car car1 = CarFactory.createCar("bmw");
		car1.run();

		Car car2 = CarFactory.createCar("ben");
		car2.run();

		//2.工厂方法模式
		CarMethodFactory carMethodFactory1 = new BmwTruckFactory();
		carMethodFactory1.createCar().run();

		CarMethodFactory carMethodFactory2 = new BenTruckFactory();
		carMethodFactory2.createCar().run();

		//3.抽象工厂模式
		AbstractCarFactory abstractCarFactory1 = new BwmFactory();
		abstractCarFactory1.createTruck().run();
		abstractCarFactory1.createSports().run();

		AbstractCarFactory abstractCarFactory2 = new BenFactory();
		abstractCarFactory2.createTruck().run();
		abstractCarFactory2.createSports().run();
	}
}
