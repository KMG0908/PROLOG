package com.spring.prolog.back;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlInjection {
	private final static String UNSECURED_CHAR_REGULAR_EXPRESSION =
			"[^\\p{Alnum}&\\.]|select|delete|update|insert|create|alter|drop";
	private Pattern unsecuredCharPattern;
	
	public void initialize() {
		unsecuredCharPattern = Pattern.compile(UNSECURED_CHAR_REGULAR_EXPRESSION, Pattern.CASE_INSENSITIVE);
	}
	
	public String makeSecureString(String str) {
		Matcher matcher = unsecuredCharPattern.matcher(str);
		
		return matcher.replaceAll("");
	}
}
