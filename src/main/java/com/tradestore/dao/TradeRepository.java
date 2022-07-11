package com.tradestore.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tradestore.model.Trade;

@Repository
public interface TradeRepository extends JpaRepository<Trade,String>{

}
