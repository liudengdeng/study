package com.summer.lambda;

public class LambdaTest {
	public static void main(String[] args) {
		LambdaTest lambdaTest = new LambdaTest();
		MathOperation mathOperation1 = (int a, int b) -> a + b;
		lambdaTest.operate(10, 5, mathOperation1);

		MathOperation mathOperation2 = (a, b) -> a - b;
		lambdaTest.operate(10, 5, mathOperation2);

		MathOperation mathOperation3 = (a, b) -> {return a * b;};
		lambdaTest.operate(10, 5, mathOperation3);

		GreetingService greetingService = message -> System.out.println(message);
		greetingService.sayMessage("runoob");
	}

	interface MathOperation {
		int operation(int a, int b);
	}

	interface GreetingService {
		void sayMessage(String message);
	}

	private void operate(int a, int b, MathOperation mathOperation) {
		int result = mathOperation.operation(a, b);
		System.out.println(result);
	}
}
