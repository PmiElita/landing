package com.hitg.util;

import org.apache.commons.codec.binary.Base64;

public class Encoder {
	public static String encodeBase64(String value){
		if (value==null)
			return null;
		return new String(Base64.encodeBase64(value.getBytes()));
	}
}
