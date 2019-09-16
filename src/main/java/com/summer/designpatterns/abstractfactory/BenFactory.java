package com.summer.designpatterns.abstractfactory;

import com.summer.designpatterns.BenSports;
import com.summer.designpatterns.BenTruck;
import com.summer.designpatterns.Car;

/**
 * ±¼³Û¹¤³§    
 * @author liu_dd
 * @date 2018/12/30 20:02
 * @version 1.0.0
 */
public class BenFactory implements AbstractCarFactory {
	public Car createTruck() {
		return new BenTruck();
	}

	public Car createSports() {
		return new BenSports();
	}
}
