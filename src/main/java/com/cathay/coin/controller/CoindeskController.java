package com.cathay.coin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cathay.coin.service.CoindeskService;
import com.cathay.coin.vo.CoinVo;
import com.cathay.coin.vo.RespVo;

/**
 * coindesk RESTful API
 * 
 * @author Kim Liao
 */
@RestController
@RequestMapping("coindesk")
public class CoindeskController {

	private final static String COINDESK_API_URL = "https://api.coindesk.com/v1/bpi/currentprice.json";

	@Autowired
	CoindeskService coindeskService;

	/**
	 * 呼叫 coindesk API, 並顯示其內容
	 * 
	 * @return {@code RespVo} 模板的 JSON 信息
	 */
	@GetMapping("raw")
	public RespVo coindeskApi() {

		RestTemplate restTemplate = new RestTemplate();
		String apiString = restTemplate.getForObject(COINDESK_API_URL, String.class);
		HashMap<String, Object> rawDataMap = coindeskService.parseRawData(apiString);

		return RespVo.OK(rawDataMap);
	}

	/**
	 * 呼叫資料轉換的 API, 並顯示其內容(每筆包含幣別,幣別中文名稱,匯率以及更新時間的資訊)
	 * 
	 * @return {@code RespVo} 模板的 JSON 信息
	 */
	@GetMapping("modified")
	public RespVo coindeskParseData() {

		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(COINDESK_API_URL, String.class);

		return RespVo.OK(coindeskService.parseDbFormat(result));
	}

	/**
	 * 新增幣別對應表資料 API
	 * 
	 * @param coinVo 前台 JSON 對應的 {@code CoinVo} 物件
	 * @param result 前台格式驗證結果集
	 * @return {@code RespVo} 模板的 JSON 信息
	 */
	@PostMapping
	public RespVo insertOne(@RequestBody @Validated CoinVo coinVo, BindingResult result) {

		if (result.hasErrors()) {
			List<String> rtnMsg = new ArrayList<>();
			List<ObjectError> allErrors = result.getAllErrors();
			for (ObjectError err : allErrors) {
				rtnMsg.add(err.getDefaultMessage());
			}

			return RespVo.NG(rtnMsg);
		}

		coindeskService.insertOne(coinVo);

		return RespVo.OK("insert OK");
	}

	/**
	 * 查詢幣別對應表資料 API
	 * 
	 * @return {@code RespVo} 模板的 JSON 信息
	 */
	@GetMapping
	public RespVo findAll() {

		return RespVo.OK(coindeskService.findAll());
	}

	/**
	 * 更新幣別對應表資料 API, 並顯示其內容
	 * 
	 * @param coinVo 前台 JSON 對應的 {@code CoinVo} 物件
	 * @param result 前台格式驗證結果集
	 * @return {@code RespVo} 模板的 JSON 信息
	 */
	@PutMapping
	public RespVo updateOne(@RequestBody @Validated CoinVo coinVo, BindingResult result) {

		if (result.hasErrors()) {
			List<String> rtnMsg = new ArrayList<>();
			List<ObjectError> allErrors = result.getAllErrors();
			for (ObjectError err : allErrors) {
				rtnMsg.add(err.getDefaultMessage());
			}

			return RespVo.NG(rtnMsg);
		}

		return RespVo.OK(coindeskService.updateByCode(coinVo));
	}

	/**
	 * 刪除幣別對應表資料 API
	 * 
	 * @param code 幣別
	 * @return {@code RespVo} 模板的 JSON 信息
	 */
	@DeleteMapping("/{code}")
	public RespVo deleteOne(@PathVariable String code) {

		Long delCnt = coindeskService.deleteByCode(code);

		if (delCnt == 0) {

			return RespVo.NG("刪除幣別(" + code + ")對應表資料失敗");
		}

		return RespVo.OK("刪除幣別(" + code + ")對應表資料成功");
	}

}
