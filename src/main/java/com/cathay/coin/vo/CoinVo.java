package com.cathay.coin.vo;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * API 接收 JSON 對應的 Value Object
 * 
 * @author Kim Liao
 * 
 */
@Data
public class CoinVo {

	@NotBlank(message = "code不能為空")
	private String code;

	@NotBlank(message = "code_cht不能為空")
	@JsonProperty("code_cht")
	private String codeCht;

	@Positive(message = "rate_float不能小於0")
	@JsonProperty("rate_float")
	private BigDecimal rateFloat;

}
