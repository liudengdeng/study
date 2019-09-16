package com.summer.designpatterns.factorymethod;

import com.summer.designpatterns.Car;

/**
 * 可以创建车的抽象工厂
 * @author liu_dd
 * @date 2018/12/30 19:25
 * @version 1.0.0
 */
public interface CarMethodFactory {
	Car createCar();
}
