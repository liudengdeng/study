package com.summer;

public class TestToStringExtends {
	public static void main(String[] args) {
		Wheat gg = new Wheat();
		System.out.println(gg);//����������д�˸����toString��������ʼ�������ӡ���������toString����

		Mill m = new Mill();
		Grain g = m.process();
		((Wheat)g).sayYe();//�ᱨ����ת���쳣
		System.out.println(g);//��ʼ��������ø����toString����
		m = new WheatMill();
		g = m.process();
		((Wheat)g).sayYe();//���ᱨ����ת���쳣
		System.out.println(g);//��ʼ������
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
