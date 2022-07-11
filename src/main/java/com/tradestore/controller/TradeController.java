package com.tradestore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tradestore.exception.InvalidTradeException;
import com.tradestore.model.Trade;
import com.tradestore.service.TradeService;


@RestController
public class TradeController {
	 @Autowired
	    TradeService tradeService;
	    
	    @RequestMapping(value = "/addTrade", method = RequestMethod.POST, consumes="application/json",produces ="application/json")
	    public ResponseEntity<String> validateStore(@RequestBody Trade trade){
	      try {
			if(tradeService.isValid(trade)) {
			       tradeService.persist(trade);
			       
			  }else{
				  return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			   }
		} catch (InvalidTradeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
			
		}
	      return ResponseEntity.status(HttpStatus.OK).build(); 
	    }

	    @GetMapping("/getTrades")
	    public List<Trade> findAllTrades(){
	        return tradeService.findAll();
	    }
}
