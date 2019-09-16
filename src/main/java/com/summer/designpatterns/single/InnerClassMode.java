package com.summer.designpatterns.single;


public class InnerClassMode {
	private InnerClassMode() {

	}

	private static class InnerSingle {
		private static InnerClassMode innerClassMode = new InnerClassMode();
	}

	public static InnerClassMode getInstance() {
		return InnerSingle.innerClassMode;
	}
}
