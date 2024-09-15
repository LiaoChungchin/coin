package com.cathay.coin.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Date;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.cathay.coin.CoinApplication;
import com.cathay.coin.dao.CoindeskDao;
import com.cathay.coin.pojo.Coindesk;

@SpringBootTest(classes = CoinApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CoindeskControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@BeforeAll
	public static void inint(@Autowired CoindeskDao coindeskDao) {
		Coindesk coindesk = new Coindesk();
		coindesk.setCode("TWD");
		coindesk.setCodeCht("新台幣");
		coindesk.setRateFloat(new BigDecimal(9.99));
		coindesk.setUpdated(new Date());
		coindeskDao.save(coindesk);
	}

	/**
	 * 測試呼叫 coindesk API, 並顯示其內容
	 * 
	 * @throws Exception
	 */
	@Order(1)
	@Test
	public void testCoindeskApi() throws Exception {

		MockHttpServletRequestBuilder requestBuilder = get("/coindesk/raw");

		// @formatter:off
		mockMvc.perform(requestBuilder)
			   .andDo(print())
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			   .andExpect(jsonPath("$.status").value(200))
			   .andExpect(jsonPath("$.message.time.updated").isNotEmpty())
			   .andExpect(jsonPath("$.message.bpi.USD").isNotEmpty())
			   .andExpect(jsonPath("$.message.bpi.GBP").isNotEmpty())
			   .andExpect(jsonPath("$.message.bpi.EUR").isNotEmpty());
		// @formatter:on
	}

	/**
	 * 測試呼叫資料轉換的 API, 並顯示其內容
	 * 
	 * @throws Exception
	 */
	@Order(2)
	@Test
	public void testCoindeskParseData() throws Exception {

		MockHttpServletRequestBuilder requestBuilder = get("/coindesk/modified");

		// @formatter:off
		mockMvc.perform(requestBuilder)
			   .andDo(print())
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			   .andExpect(jsonPath("$.status").value(200))
			   .andExpect(jsonPath("$.message",hasSize(3)))
			   .andExpect(jsonPath("$.message[0].code").value("USD"))
			   .andExpect(jsonPath("$.message[0].updated").isNotEmpty())
			   .andExpect(jsonPath("$.message[0].code_cht").isNotEmpty())
			   .andExpect(jsonPath("$.message[0].rate_float").isNotEmpty())
			   .andExpect(jsonPath("$.message[1].code").value("GBP"))
			   .andExpect(jsonPath("$.message[1].updated").isNotEmpty())
			   .andExpect(jsonPath("$.message[1].code_cht").isNotEmpty())
			   .andExpect(jsonPath("$.message[1].rate_float").isNotEmpty())
			   .andExpect(jsonPath("$.message[2].code").value("EUR"))
			   .andExpect(jsonPath("$.message[2].updated").isNotEmpty())
			   .andExpect(jsonPath("$.message[2].code_cht").isNotEmpty())
			   .andExpect(jsonPath("$.message[2].rate_float").isNotEmpty());
		// @formatter:on
	}

	/**
	 * 測試呼叫新增幣別對應表資料 API
	 * 
	 * @throws Exception
	 */
	@Order(3)
	@Test
	public void testInsertOne() throws Exception {

		// @formatter:off
		JSONObject bodyRawJson = new JSONObject()
										.put("code", "CNY")
										.put("code_cht","人民幣")
										.put("rate_float", 6.66);
		
		MockHttpServletRequestBuilder requestBuilder = post("/coindesk")
														.contentType(MediaType.APPLICATION_JSON)
														.content(bodyRawJson.toString());
		
		mockMvc.perform(requestBuilder)
			   .andDo(print())
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			   .andExpect(jsonPath("$.status").value(200))
			   .andExpect(jsonPath("$.message").value("insert OK"));
		// @formatter:on
	}

	/**
	 * 測試呼叫查詢幣別對應表資料 API, 並顯示其內容
	 * 
	 * @throws Exception
	 */
	@Order(4)
	@Test
	public void testFindAll() throws Exception {

		MockHttpServletRequestBuilder requestBuilder = get("/coindesk");

		// @formatter:off
		mockMvc.perform(requestBuilder)
			   .andDo(print())
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			   .andExpect(jsonPath("$.status").value(200))
			   .andExpect(jsonPath("$.message",hasSize(2)))
			   .andExpect(jsonPath("$.message[0].code").value("TWD"))
			   .andExpect(jsonPath("$.message[0].code_cht").value("新台幣"))
			   .andExpect(jsonPath("$.message[0].rate_float").value(9.99))
			   .andExpect(jsonPath("$.message[0].updated").isNotEmpty())
			   .andExpect(jsonPath("$.message[1].code").value("CNY"))
			   .andExpect(jsonPath("$.message[1].code_cht").value("人民幣"))
			   .andExpect(jsonPath("$.message[1].rate_float").value(6.66))
			   .andExpect(jsonPath("$.message[1].updated").isNotEmpty());
		// @formatter:on
	}

	/**
	 * 測試呼叫更新幣別對應表資料 API, 並顯示其內容
	 * 
	 * @throws Exception
	 */
	@Order(5)
	@Test
	public void testUpdateOne() throws Exception {

		// @formatter:off
		JSONObject bodyRawJson = new JSONObject()
									.put("code", "CNY")
									.put("code_cht","人民幣(改)")
									.put("rate_float", 7.77);

		MockHttpServletRequestBuilder requestBuilder = put("/coindesk")
														.contentType(MediaType.APPLICATION_JSON)
														.content(bodyRawJson.toString());
		
		mockMvc.perform(requestBuilder)
			   .andDo(print())
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			   .andExpect(jsonPath("$.status").value(200))
			   .andExpect(jsonPath("$.message.code").value("CNY"))
			   .andExpect(jsonPath("$.message.code_cht").value("人民幣(改)"))
			   .andExpect(jsonPath("$.message.rate_float").value(7.77))
			   .andExpect(jsonPath("$.message.updated").isNotEmpty());
		// @formatter:on
	}

	/**
	 * 測試呼叫刪除幣別對應表資料 API
	 * 
	 * @throws Exception
	 */
	@Order(6)
	@Test
	public void tsetDeleteOne() throws Exception {

		MockHttpServletRequestBuilder requestBuilder = delete("/coindesk/CNY");

		// @formatter:off
		mockMvc.perform(requestBuilder)
			   .andDo(print())
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			   .andExpect(jsonPath("$.status").value(200))
			   .andExpect(jsonPath("$.message").value("刪除幣別(CNY)對應表資料成功"));
		// @formatter:on

		requestBuilder = get("/coindesk");

		// @formatter:off
		mockMvc.perform(requestBuilder)
			   .andDo(print())
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			   .andExpect(jsonPath("$.status").value(200))
			   .andExpect(jsonPath("$.message",hasSize(1)));
		// @formatter:on
	}
}
