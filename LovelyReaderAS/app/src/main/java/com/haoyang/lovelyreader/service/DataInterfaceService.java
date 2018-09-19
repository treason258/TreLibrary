/**
 * 
 */
package com.haoyang.lovelyreader.service;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 *
 * 
 * @author tianyu912@yeah.net
 */
public class DataInterfaceService {

	private static Map<String, String> urlMap = new HashMap<String, String>();

	public static final String SYN = "syn";
	public static final String LOGIN = "login";
	public static final String REGISTER = "register";
	public static final String FINDPASSWORD = "findPassword";
	public static final String FEEDBACK = "feedBack";
	public static final String DELETEBOOK = "deleteBook";
	public static final String UPDATEBOOK = "updateBook";
	public static final String loadByUser = "loadByUser";
	public static final String ADD_CATEGORY = "addCategory";

	static {

		urlMap.put(loadByUser, "/app/address/loadAddressJson");
		urlMap.put(LOGIN, "/app/user/loginForJson");
		urlMap.put(REGISTER, "/app/user/register");
		urlMap.put(ADD_CATEGORY, "/app/tree/addNodeFromMobile");
	}

	private static String base = "http://112.126.80.1:8080/WebSiteService/";

	public static String getUrl(String urlKey) {

		String u = urlMap.get(urlKey);
		return base + u;
	}
}
