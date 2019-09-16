package com.summer.designpatterns.single;

/**
 * ����ʽ����
 * �̰߳�ȫ
 * @author liu_dd
 * @date 2019/1/1 15:20
 * @version 1.0.0
 */
public class LazyMode1 {
	private LazyMode1() {

	}

	private static LazyMode1 lazyMode = null;
	private static volatile LazyMode1 lazyMode2 = null;

	public static synchronized LazyMode1 getInstance() {
		if (lazyMode == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lazyMode = new LazyMode1();
		}
		return lazyMode;
	}

	//public static synchronized LazyMode1 getInstance()�����ַ�ʽһ�����̰߳�ȫ
	public static LazyMode1 getInstance1() {
		synchronized (LazyMode1.class) {
			if (lazyMode == null) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				lazyMode = new LazyMode1();
			}
			return lazyMode;
		}
	}

	//�̲߳���ȫ
	public static LazyMode1 getInstance2() {
			if (lazyMode == null) {
				synchronized (LazyMode1.class) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					lazyMode = new LazyMode1();
				}
			}
			return lazyMode;
	}

	/**
	 * �̰߳�ȫ,˫�������ƿ�����û�����⣬��Ϊ����һ���µĶ�����ԭ���Բ�����
	 * 1.�����ڴ�
	 * 2.��ʼ��������
	 * 3.������ָ�������ڴ��ַ
	 * ����jvm���ſ��ܻὫ3��2���������ȰѶ����ַ��ֵ�������ˣ���δ��ʼ����������ʱ�����������߳������getInstance�����ж�lazyModeΪnull
	 */
	public static LazyMode1 getInstance3() {
		if (lazyMode == null) {
			synchronized (LazyMode1.class) {
				if (lazyMode == null) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					lazyMode = new LazyMode1();
				}
			}
		}
		return lazyMode;
	}

	/**
	 * �÷�ʽ�Ż���getInstance3�����̲߳���ȫ�����
	 * lazyMode2ʵ������ǰ�����volatile��
	 * ��ֹ��JVM�Զ���ָ���������Ż���ǿ�б�֤�߳��жԱ����������κ�д������������̶߳��Ǽ�ʱ�ɼ��ģ�
	 */
	public static LazyMode1 getInstance4() {
		if (lazyMode == null) {
			synchronized (LazyMode1.class) {
				if (lazyMode == null) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					lazyMode2 = new LazyMode1();
				}
			}
		}
		return lazyMode;
	}
}
