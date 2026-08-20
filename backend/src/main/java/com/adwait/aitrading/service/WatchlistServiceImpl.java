package com.adwait.aitrading.service;

import com.adwait.aitrading.model.Coin;
import com.adwait.aitrading.model.User;
import com.adwait.aitrading.model.Watchlist;
import com.adwait.aitrading.repository.WatchlistRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WatchlistServiceImpl implements WatchlistService{

    private final WatchlistRepository watchlistRepository;

    public WatchlistServiceImpl(WatchlistRepository watchlistRepository) {
        this.watchlistRepository = watchlistRepository;
    }

    @Override
    public Watchlist findUserWatchlist(Long userId) throws Exception {

        Watchlist watchlist = watchlistRepository.findByUserId(userId);
        if(watchlist == null){
            throw new Exception("Watchlist not found.");
        }
        return watchlist;
    }

    @Override
    public Watchlist createWatchlist(User user) {

        Watchlist watchlist =new Watchlist();
        watchlist.setUser(user);
        return watchlistRepository.save(watchlist);
    }

    @Override
    public Watchlist findById(Long id) throws Exception {

        Optional<Watchlist> optionalWatchlist = watchlistRepository.findById(id);
        if(optionalWatchlist.isEmpty()){
            throw new Exception("Watchlist not found.");
        }
        return optionalWatchlist.get();
    }

    @Override
    public Coin addItemToWatchlist(Coin coin, User user) throws Exception {

        Watchlist watchlist =findUserWatchlist(user.getId());
        if(watchlist.getCoins().contains(coin)){
            watchlist.getCoins().remove(coin);
        }
        else{
            watchlist.getCoins().add(coin);
        }
         watchlistRepository.save(watchlist);
        return coin;
    }
}
