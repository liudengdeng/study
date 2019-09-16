package com.summer.iterfce;

public class Apply {
}

class Processor {
	public String name() {
		return getClass().getSimpleName();
	}

	Object process(Object input) {
		return input;

	}
}

class Upcase extends Processor {
	@Override
	String process(Object input) {
		return ((String) input).toUpperCase();
	}
}

class Downcase extends Processor {
	@Override
	String process(Object input) {
		return ((String) input).toLowerCase();
	}
}
