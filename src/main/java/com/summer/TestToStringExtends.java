package com.summer;

public class TestToStringExtends {
	public static void main(String[] args) {
		Wheat gg = new Wheat();
		System.out.println(gg);//由于子类重写了父类的toString方法，初始化父类打印的是子类的toString方法

		Mill m = new Mill();
		Grain g = m.process();
		((Wheat)g).sayYe();//会报错类转型异常
		System.out.println(g);//初始化父类调用父类的toString方法
		m = new WheatMill();
		g = m.process();
		((Wheat)g).sayYe();//不会报错类转型异常
		System.out.println(g);//初始化子类
	}

}

class Grain {
	Grain() {
		System.out.println(toString());
	}
	@Override
	public String toString() {
		return "grain";
	}
}

class Wheat extends Grain {
	@Override
	public String toString() {
		return "wheat";
	}

	public void sayYe() {
		System.out.println("yyeyey");
	}
}

class Mill {
	Grain process() {
		return new Grain();
	}
}

class WheatMill extends Mill {
	@Override
	Wheat process() {
		return new Wheat();
	}
}
