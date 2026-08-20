package com.adwait.aitrading.service;

import com.adwait.aitrading.model.CoinDTO;
import com.adwait.aitrading.response.ApiResponse;
public interface ChatBotService {

    ApiResponse getCoinDetails(String coinName);

    CoinDTO getCoinByName(String coinName);

    String simpleChat(String prompt);
}
