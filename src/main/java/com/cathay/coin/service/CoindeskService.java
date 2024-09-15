package com.cathay.coin.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cathay.coin.dao.CoindeskDao;
import com.cathay.coin.pojo.Coindesk;
import com.cathay.coin.utils.CoinJsonUtil;
import com.cathay.coin.vo.CoinVo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Coindesk 相關的業務邏輯
 * 
 * @author Kim Liao
 */
@Service
public class CoindeskService {

	private final ObjectMapper objectMapper;

	@Autowired
	private CoindeskDao coindeskDao;

	public CoindeskService(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
	}

	/**
	 * 直接回傳 Coindesk API 的原始資料
	 * 
	 * @param json API 回傳的 JSON 字串
	 * @return 以 {@code Map<String, Object>} 方式顯示資料
	 */
	public HashMap<String, Object> parseRawData(String json) {

		TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
		};

		try {
			return objectMapper.readValue((String) json, typeRef);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Coindesk API 資訊取得異常");
		}
	}

	/**
	 * 將 Coindesk API 資料進行整理,每筆包含(幣別,幣別中文名稱,匯率以及更新時間)
	 * 
	 * @param json API 回傳的 JSON 字串
	 * @return 以 {@code List<Coindesk>} 回傳清單
	 */
	public List<Coindesk> parseDbFormat(String json) {

		try {
			return CoinJsonUtil.asList(json, objectMapper);
		} catch (Exception e) {
			throw new RuntimeException("Coindesk API 資訊轉換異常");
		}
	}

	/**
	 * DB 新增一筆 {@code Coindesk} 資料
	 * 
	 * @param coinVo 前台 json 對應的 {@code CoinVo} 物件
	 */
	public void insertOne(CoinVo coinVo) {

		Coindesk coindesk = new Coindesk();

		coindesk.setCode(coinVo.getCode());
		coindesk.setCodeCht(coinVo.getCodeCht());
		coindesk.setRateFloat(coinVo.getRateFloat());
		coindesk.setUpdated(new Date());

		coindeskDao.save(coindesk);
	}

	/**
	 * 查詢 DB 所有 coindesk 資料
	 * 
	 * @return 以 {@code List<Coindesk>} 回傳 DB 資料
	 */
	public List<Coindesk> findAll() {

		return coindeskDao.findAll();
	}

	/**
	 * 依照幣別刪除一筆 DB 的 coindesk 資料
	 * 
	 * @param code 幣別
	 * @return 刪除筆數
	 */
	@Transactional
	public Long deleteByCode(String code) {

		return coindeskDao.deleteByCode(code);
	}

	/**
	 * 依照幣別更新一筆 DB 的 coindesk 資料
	 * 
	 * @param coinVo 前台 json 對應的 {@code CoinVo} 物件
	 * @return 更新完後的 {@code Coindesk} 物件
	 */
	@Transactional
	public Coindesk updateByCode(CoinVo coinVo) {

		String targetCode = coinVo.getCode();
		Optional<Coindesk> optional = coindeskDao.findOneByCode(targetCode);

		Coindesk coindesk = optional.orElseThrow(() -> new RuntimeException("DB 查無幣別(" + targetCode + ")資料"));
		coindesk.setCodeCht(coinVo.getCodeCht());
		coindesk.setRateFloat(coinVo.getRateFloat());
		coindesk.setUpdated(new Date());

		return coindeskDao.save(coindesk);
	}

}
