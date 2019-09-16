package com.summer.designpatterns.abstractfactory;

import com.summer.designpatterns.BmwSports;
import com.summer.designpatterns.BmwTruck;
import com.summer.designpatterns.Car;

/**
 * ±¦Âí¹¤³§    
 * @author liu_dd
 * @date 2018/12/30 20:01
 * @version 1.0.0
 */
public class BwmFactory implements AbstractCarFactory {

	public Car createTruck() {
		return new BmwTruck();
	}

	public Car createSports() {
		return new BmwSports();
	}
}
