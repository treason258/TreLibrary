/**
 * 
 */
package com.app.base.service.business;

import java.util.HashMap;
import java.util.Map;

/**
 * 错误描述类。
 * @author tianyu
 */
public class Error {

	private static final Map<String, String> serverStatusMessage = new HashMap<String, String>();

	public ErrorType errorType;

	public int errorCode;

	public String message;

	static {

		serverStatusMessage.put("1", "您已经长时间没有使用，是否继续？");
		serverStatusMessage.put("2", "无权访问");
		serverStatusMessage.put("3", "Json格式错误");
		serverStatusMessage.put("4", "服务端出错");
		serverStatusMessage.put("5", "重复数据");
		serverStatusMessage.put("6", "用户不存在");
		serverStatusMessage.put("7", "密码错误");
		serverStatusMessage.put("8", "参数错误");
		serverStatusMessage.put("9", "无结果集");
		serverStatusMessage.put("10", "该用户名已经存在，请使用其它用户名");
		serverStatusMessage.put("11", "该邮箱已被注册，请使用其它邮箱");
		serverStatusMessage.put("12", "该用户名、邮箱均已被注册，请使用其它用户名、邮箱");
		serverStatusMessage.put("13", "选课尚未完成，不能开始学习");
		serverStatusMessage.put("14", "该课件已存在，请选择其他课件");
		serverStatusMessage.put("15", "旧密码错误");
		serverStatusMessage.put("16", "该用户已对评论点过赞");
		serverStatusMessage.put("17", "对不起，您已经评论过了，不能再次评论");
		serverStatusMessage.put("18", "用户名含有非法字符(输入字符长度为6-20位，只支持，字母、数字、下划线)");

	}

}
