package com.adwait.aitrading.service;

import com.adwait.aitrading.model.Asset;
import com.adwait.aitrading.model.Coin;
import com.adwait.aitrading.model.User;
import com.adwait.aitrading.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImpl implements AssetService{


    private final AssetRepository assetRepository;

    @Autowired
    public AssetServiceImpl(AssetRepository assetRepository) {

        this.assetRepository = assetRepository;
    }
    @Override
    public Asset createAsset(User user, Coin coin, double quantity) {
        Asset asset = new Asset();
        asset.setUser(user);
        asset.setCoin(coin);
        asset.setQuantity(quantity);
        asset.setBuyPrice(coin.getCurrentPrice());

        return assetRepository.save(asset);
    }

    @Override
    public Asset getAssetById(Long assetId){

        return assetRepository.findById(assetId)
                .orElseThrow(()->new IllegalArgumentException("Asset not found."));
    }

    @Override
    public Asset getAssetByUserIdAndId(Long userId, Long assetId) {

        return assetRepository.findByIdAndUserId(assetId, userId);
    }

    @Override
    public List<Asset> getUsersAssets(Long userId) {

        return assetRepository.findByUserId(userId);
    }

    @Override
    public Asset updateAsset(Long assetId, double quantity) throws Exception {

        Asset oldAsset = getAssetById(assetId);

        if(oldAsset == null){
            throw new Exception("Asset not found.");
        }
        oldAsset.setQuantity(quantity + oldAsset.getQuantity());
        return assetRepository.save(oldAsset);

    }

    @Override
    public Asset findAssetByUserIdAndCoinId(Long userId, String coinId) {
        return assetRepository.findByUserIdAndCoinId(userId, coinId);
    }

    @Override
    public void deleteAsset(Long assetId) {

        assetRepository.deleteById(assetId);

    }
}
