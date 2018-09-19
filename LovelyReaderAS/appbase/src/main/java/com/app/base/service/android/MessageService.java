/**
 * 
 */
package com.app.base.service.android;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * 
 * @author Administrator email:tianyu912@yeah.net<br>
 * 
 *         date:2012年1月1日 上午4:24:18 <br>
 */
public class MessageService {

	private static MessageService service;

	/**
	 * key=>statusId, value=>messageResourceId
	 */
	private final Map<Integer, Integer> statusIdToResourceId = new HashMap<Integer, Integer>();
	private final int[] sorryPhrases = { 
//			R.string.sorry1, R.string.sorry2,
//			R.string.sorry3
			};

	private final Map<String, String> serverStatusMessage = new HashMap<String, String>();

	private MessageService() {
		init();
	}

	private void init() {
//		statusIdToResourceId.put(Status.CONNECT_TIMEOUT,
//				R.string.msgLoginTimeout);
//		statusIdToResourceId.put(Status.NET_READ_WRITE_ERROR,
//				R.string.netReadWriteError);
//		statusIdToResourceId.put(Status.DATA_PARSE_ERROR,
//				R.string.dataParseError);
//		statusIdToResourceId.put(Status.HTTP_URL_MAKE_ERROR,
//				R.string.msgHttpUrlError);
//		statusIdToResourceId.put(Status.CONNECT_REJECT_ERROR,
//				R.string.connectRrejectError);

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

	public static MessageService instance() {

		if (service == null) {
			service = new MessageService();
		}
		return service;
	}

	public String getMessageFromExceptionStatus(int statusId) {

		Integer resourceId = statusIdToResourceId.get(statusId);
		if (resourceId != null) {

			Random random = new Random();
			int sorryPhraseId = Math.abs(random.nextInt() % sorryPhrases.length);

			return App.getContext().getResources().getString(
					sorryPhrases[sorryPhraseId])
					+ App.getContext().getResources().getString(resourceId);
		} else {
			return "未知错误!!";
		}
	}

	public String getMessageFromExceptionStatus(int statusId, int step) {

		Integer resourceId = statusIdToResourceId.get(statusId);
		String stepMessage = App.getContext().getResources().getString(step);
		if (stepMessage == null) {
			stepMessage = "";
		}

		if (resourceId != null) {

			Random random = new Random();
			int sorryPhraseId = random.nextInt() % sorryPhrases.length;

			return App.getContext().getResources().getString(
					sorryPhrases[sorryPhraseId])
					+ App.getContext().getResources().getString(resourceId);
		} else {
			return "未知错误!!";
		}
	}

	public String getMessageByServerStatus(String statusId) {

		String message = serverStatusMessage.get(statusId);
		if (message == null) {
			return "未知错误!!";
		}
		return message;
	}
}
