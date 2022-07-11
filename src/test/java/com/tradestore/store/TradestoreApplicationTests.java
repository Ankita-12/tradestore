package com.tradestore.store;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.tradestore.controller.TradeController;
import com.tradestore.exception.InvalidTradeException;
import com.tradestore.model.Trade;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TradestoreApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private TradeController tradeController;

	@Test
	void testIfTradeIsValid() {
		ResponseEntity responseEntity = tradeController.validateStore(createTrade("T1",1,LocalDate.now()));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(),responseEntity);
		List<Trade> tradeList = tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList.size());
		Assertions.assertEquals("T1",tradeList.get(0).getTradeId());
	}

	@Test
	void testIfTradeMaturityDateHasPast() {
			LocalDate localDate = getLocalDate(2015, 05, 21);
			ResponseEntity responseEntity = tradeController.validateStore(createTrade("T2", 1, localDate));
			Assertions.assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).build(),responseEntity);
	}

	@Test
	void testIfTradeVersionIsLessThanOldVersion() {
		// step-1 create trade
		ResponseEntity responseEntity = tradeController.validateStore(createTrade("T1",2,getLocalDate(2022, 07, 21)));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(),responseEntity);
		ResponseEntity responseEntity1 = tradeController.validateStore(createTrade("T1", 1, getLocalDate(2022, 07, 21)));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build(),responseEntity1);
	}

	@Test
	void testIfTradeHasSameVersionId(){
		ResponseEntity responseEntity = tradeController.validateStore(createTrade("T1",2,LocalDate.now()));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(),responseEntity);
		List<Trade> tradeList = tradeController.findAllTrades();
		Trade trade2 = createTrade("T1",2,LocalDate.now());
		ResponseEntity responseEntity2 = tradeController.validateStore(trade2);
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(),responseEntity2);
		List<Trade> tradeList2 =tradeController.findAllTrades();
		Assertions.assertEquals(tradeList.get(0).getTradeId(),tradeList2.get(0).getTradeId());
	}
	
	private Trade createTrade(String tradeId,int version,LocalDate  maturityDate){
		Trade trade = new Trade();
		trade.setTradeId(tradeId);
		trade.setBookId(tradeId+"B1");
		trade.setVersion(version);
		trade.setCounterParty(tradeId+"Cpty");
		trade.setMaturityDate(maturityDate);
		trade.setCreatedDate(LocalDate.now());
		trade.setExpiredFlag("Y");
		return trade;
	}

	public static LocalDate getLocalDate(int year,int month, int day){
		LocalDate localDate = LocalDate.of(year,month,day);
		return localDate;
	}


}
