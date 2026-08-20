package com.adwait.aitrading.service;

import com.adwait.aitrading.model.Coin;
import com.adwait.aitrading.model.User;
import com.adwait.aitrading.model.Watchlist;

public interface WatchlistService {

    Watchlist findUserWatchlist(Long userId) throws Exception;

    Watchlist createWatchlist(User user);

    Watchlist findById(Long id) throws Exception;

    Coin addItemToWatchlist(Coin coin, User user) throws Exception;
}
