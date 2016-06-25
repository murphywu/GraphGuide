package com.example.wutao.graphguide.utils;

import java.util.Collection;

public class Utils {
	public static boolean isEmpty(CharSequence text){
		return text == null || text.equals("");
	}
	
	public static boolean isEmpty(Object[] array) {
		return array == null || array.length <= 0;
	}
	
	public static boolean isEmpty(char[] charArray) {
		return charArray == null || charArray.length <= 0;
	}
	
	public static char toUpperCase(char c){
		return String.valueOf(c).toUpperCase().charAt(0);
	}
	
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.size() == 0;
	}
}
