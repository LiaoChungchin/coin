package com.cathay.coin.pojo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * DB table Coindesk ORMapping
 * 
 * @author Kim Liao
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "coin_desk")
public class Coindesk {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "code", unique = true, nullable = false)
	private String code;

	@JsonProperty("code_cht")
	@Column(name = "code_cht", nullable = false)
	private String codeCht;

	@JsonProperty("rate_float")
	@Column(name = "rate_float", nullable = false)
	private BigDecimal rateFloat;

	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "updated", nullable = false)
	private Date updated;

}
