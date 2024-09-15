package com.cathay.coin.exception;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cathay.coin.vo.RespVo;

/**
 * 系統發生異常時, 統一回傳的 JSON 格式
 * 
 * @author Kim Liao
 */
@RestControllerAdvice
public class CoinExceptionHandler {

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public RespVo handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

		return RespVo.NG("JSON輸入格式解析異常");
	}

	@ExceptionHandler(Exception.class)
	public RespVo handleException(Exception e) {

		return RespVo.NG(e.getMessage());
	}

}
