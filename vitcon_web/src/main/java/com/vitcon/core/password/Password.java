package com.vitcon.core.password;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Password {

	/**
	 * <p>
	 * 입력한 데이터(바이트 배열)을 SHA1 알고리즘으로 처리하여 해쉬값을 도출한다.
	 * </p>
	 * 
	 * <pre>
	 * getHash([0x68, 0x61, 0x6e]) = [0x4f, 0xf6, 0x15, 0x25, 0x34, 0x69, 0x98, 0x99, 0x32, 0x53, 0x2e, 0x92, 0x60, 0x06, 0xae, 0x5c, 0x99, 0x5e, 0x5d, 0xd6]
	 * </pre>
	 * 
	 * @param input 입력 데이터(<code>null</code>이면 안된다.)
	 * @return 해쉬값
	 */
	public static byte[] getHash(byte[] input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			return md.digest(input);
		} catch (NoSuchAlgorithmException e) {
			// 일어날 경우가 없다고 보지만 만약을 위해 Exception 발생
			throw new RuntimeException("SHA1" + " Algorithm Not Found", e);
		}
	}
	
	private static String toHexString(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		
		StringBuffer result = new StringBuffer();
		for (byte b : bytes) {
			result.append(Integer.toString((b & 0xF0) >> 4, 16));
			result.append(Integer.toString(b & 0x0F, 16));
		}
		return result.toString();
	}

	/**
	 * <p>
	 * MySQL 의 PASSWORD() 함수.
	 * </p>
	 * 
	 * <pre>
	 * MySqlFunction.password(null)                    = null
	 * MySqlFunction.password("mypassword".getBytes()) = "*FABE5482D5AADF36D028AC443D117BE1180B9725"
	 * </pre>
	 * 
	 * @param input
	 * @return
	 */
	public static String password(byte[] input) {
		byte[] digest = null;

		// Stage 1
		digest = getHash(input);
		// Stage 2
		digest = getHash(digest);

		StringBuilder sb = new StringBuilder(1 + digest.length);
		sb.append("*");
		sb.append(toHexString(digest).toUpperCase());
		return sb.toString();
	}

	/**
	 * <p>
	 * MySQL 의 PASSWORD() 함수.
	 * </p>
	 * 
	 * <pre>
	 * MySqlFunction.password(null)         = null
	 * MySqlFunction.password("mypassword") = "*FABE5482D5AADF36D028AC443D117BE1180B9725"
	 * </pre>
	 * 
	 * @param input
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String password(String input) {
		if (input == null) {
			return null;
		}
		return password(input.getBytes());
	}

	/**
	 * <p>
	 * MySQL 의 PASSWORD() 함수.
	 * </p>
	 * 
	 * <pre>
	 * MySqlFunction.password(null, *)                    = null
	 * MySqlFunction.password("mypassword", "ISO-8859-1") = "*FABE5482D5AADF36D028AC443D117BE1180B9725"
	 * </pre>
	 * 
	 * @param input
	 * @param charsetName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String password(String input, String charsetName) throws UnsupportedEncodingException {
		if (input == null) {
			return null;
		}
		return password(input.getBytes(charsetName));
	}

}