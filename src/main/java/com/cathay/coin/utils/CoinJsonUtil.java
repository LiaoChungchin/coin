package com.cathay.coin.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.cathay.coin.pojo.Coindesk;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Coindesk 的 JSON 轉換工具包
 * 
 * @author Kim Liao
 * 
 */
public class CoinJsonUtil {

	/**
	 * 將 Coindesk API 回傳的 JSON 轉換成 {@code Coindesk} 物件
	 * 
	 * @param json API 回傳的 JSON
	 * @param mapper {@code ObjectMapper} 解析器
	 * @return {@code Coindesk} 所組成的 {@code List} 清單
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 * @throws ParseException
	 */
	public static List<Coindesk> asList(String json, ObjectMapper mapper)
			throws JsonMappingException, JsonProcessingException, ParseException {

		List<Coindesk> rtnList = new ArrayList<>();

		JsonNode jsonNode = mapper.readTree(json);

		String updated = jsonNode.get("time").get("updated").asText();
		Date updatedDate = DataUtil.parseToDate(updated);

		Iterator<String> fieldNames = jsonNode.get("bpi").fieldNames();
		while (fieldNames.hasNext()) {
			String coinName = fieldNames.next();
			Coindesk coinData = mapper.readValue(jsonNode.get("bpi").get(coinName).toString(), Coindesk.class);

			if ("USD".equals(coinName)) {
				coinData.setCodeCht("美金");
			} else if ("GBP".equals(coinName)) {
				coinData.setCodeCht("英鎊");
			} else if ("EUR".equals(coinName)) {
				coinData.setCodeCht("歐元");
			}
			coinData.setUpdated(updatedDate);

			rtnList.add(coinData);
		}

		return rtnList;
	}

}
