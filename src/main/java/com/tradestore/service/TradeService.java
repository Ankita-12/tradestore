package com.tradestore.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tradestore.dao.TradeRepository;
import com.tradestore.exception.InvalidTradeException;
import com.tradestore.model.Trade;

@Service
public class TradeService {
	 private static final Logger log = LoggerFactory.getLogger(TradeService.class);

	    @Autowired
	    TradeRepository tradeRepository;

	    public boolean isValid(Trade trade)throws InvalidTradeException{
	        if(isMaturityDateValid(trade)) {	 
	             Optional<Trade> exsitingTrade = tradeRepository.findById(trade.getTradeId());
	             if (exsitingTrade.isPresent()) {
	                 return isVersionValid(trade, exsitingTrade.get());
	             }else{
	                 return true;
	             }
	         }
	         return false;
	    }

	    private boolean isVersionValid(Trade trade,Trade oldTrade) throws InvalidTradeException {
	        if(trade.getVersion() == oldTrade.getVersion()){
	            return true;
	        }
	        if(trade.getVersion() < oldTrade.getVersion()) {
	            throw new InvalidTradeException("trade Rejected");
	        }
	        
	        return false;
	    }

	    private boolean isMaturityDateValid(Trade trade){
	        return trade.getMaturityDate().isBefore(LocalDate.now())  ? false:true;
	    }

	    public void  persist(Trade trade){ 
	        trade.setCreatedDate(LocalDate.now());
	        tradeRepository.save(trade);
	    }

	    public List<Trade> findAll(){
	       return  tradeRepository.findAll();
	    }
}
