package com.summer.classloader;

import java.io.IOException;
import java.io.InputStream;

public class ClassLoaderTest {
	public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		ClassLoader classLoader = new ClassLoader() {
			@Override
			public Class<?> loadClass(String name) throws ClassNotFoundException {
				try {
					String fileName = name.substring(name.lastIndexOf(".")+1)+".class";
					System.out.println(fileName);
					InputStream is = getClass().getResourceAsStream(fileName);
					if (null == is) {
						return super.loadClass(name);
					}
					byte[] b = new byte[is.available()];
					is.read(b);
					return defineClass(name, b, 0, b.length);
				} catch (IOException e) {
					throw new ClassNotFoundException(name);
				}
			}
		};

//		Object obj = classLoader.loadClass("com.summer.Testq").newInstance();
//		System.out.println(obj.getClass());
//		System.out.println(obj instanceof com.summer.Testq);

		Object obj2 = classLoader.loadClass("com.summer.classloader.ClassLoaderTest").newInstance();
		System.out.println(obj2.getClass());
		System.out.println(obj2 instanceof com.summer.classloader.ClassLoaderTest);

	}
}
