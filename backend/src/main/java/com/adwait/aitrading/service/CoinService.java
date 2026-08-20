package com.adwait.aitrading.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.adwait.aitrading.model.Coin;

import java.util.List;

public interface CoinService {
    List<Coin> getCoinList(int page) throws Exception;

    String getMarketChart(String coinId, int days) throws Exception;

    // String getCoinDetails(String coinId) throws Exception;
    String getCoinDetails(String coinId) throws JsonProcessingException;

    Coin findById(String coinId) throws Exception;

    // String searchCoin(String keyword) throws Exception;
    String searchCoin(String keyword);

    // String getTop50CoinsByMarketCapRank() throws Exception;
    String getTop50CoinsByMarketCapRank();

    // String getTrendingCoins() throws Exception;
    String getTrendingCoins();
}
