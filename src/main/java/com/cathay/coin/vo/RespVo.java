package com.cathay.coin.vo;

import lombok.Data;

/**
 * 統一格式的 JSON 回傳模板
 * 
 * @author Kim Liao
 * 
 */
@Data
public class RespVo {

	private int status = 200;
	private Object message = "OK";

	/**
	 * 一般正常的回傳模板
	 * 
	 * @param message 回傳內容
	 * @return JSON字串
	 */
	public static RespVo OK(Object message) {

		RespVo respVo = new RespVo();
		respVo.setMessage(message);

		return respVo;
	}

	/**
	 * 錯誤發生的回傳模板
	 * 
	 * @param message 錯誤信息
	 * @return JSON字串
	 */
	public static RespVo NG(Object message) {

		RespVo respVo = new RespVo();
		respVo.setStatus(500);
		respVo.setMessage(message);

		return respVo;
	}

}
