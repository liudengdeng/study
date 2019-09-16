package com.summer.designpatterns.abstractfactory;

import com.summer.designpatterns.Car;

/**
 * ���Դ������ĳ��󹤳�
 * @author liu_dd
 * @date 2018/12/30 19:58
 * @version 1.0.0
 */
public interface AbstractCarFactory {
	Car createTruck();

	Car createSports();
}
