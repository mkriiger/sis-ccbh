package com.castaware.nocturne.api.domain;

import java.math.BigDecimal;

import com.castaware.nocturne.api.BinanceException;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

// Internal variables are not human readable. So this class contains better readable getters

import com.google.gson.JsonArray;

import lombok.Data;

/*
  [
    1499040000000,      // 0 Open time
    "0.01634790",       // 1 Open
    "0.80000000",       // 2 High
    "0.01575800",       // 3 Low
    "0.01577100",       // 4 Close
    "148976.11427815",  // 5 Volume
    1499644799999,      // 6 Close time
    "2434.19055334",    // 7 Quote asset volume
    308,                // 8 Number of trades
    "1756.87402397",    // 9 Taker buy base asset volume
    "28.46694368",      // 10 Taker buy quote asset volume
    "17928899.62484339" // 11 Can be ignored
  ]
*/

@Data
public class BinanceCandlestick {

    public Long openTime = null;
    public BigDecimal open = null;
    public BigDecimal high = null;
    public BigDecimal low = null;
    public BigDecimal close = null;
    public BigDecimal volume = null;
    public Long closeTime = null;
    public BigDecimal quoteAssetVolume = null;
    public Long numberOfTrades = null;
    public BigDecimal takerBuyBaseAssetVolume = null;
    public BigDecimal takerBuyQuoteAssetVolume = null;

    public BinanceCandlestick(JsonArray jsonArray) throws BinanceException {
        if (jsonArray.size() < 11) {
            throw new BinanceException("Error reading candlestick, 11 parameters expected, "
                    + jsonArray.size() + " found");
        }
        setOpenTime(jsonArray.get(0).getAsLong());
        setOpen(jsonArray.get(1).getAsBigDecimal());
        setHigh(jsonArray.get(2).getAsBigDecimal());
        setLow(jsonArray.get(3).getAsBigDecimal());
        setClose(jsonArray.get(4).getAsBigDecimal());
        setVolume(jsonArray.get(5).getAsBigDecimal());
        setCloseTime(jsonArray.get(6).getAsLong());
        setQuoteAssetVolume(jsonArray.get(7).getAsBigDecimal());
        setNumberOfTrades(jsonArray.get(8).getAsLong());
        setTakerBuyBaseAssetVolume(jsonArray.get(9).getAsBigDecimal());
        setTakerBuyQuoteAssetVolume(jsonArray.get(10).getAsBigDecimal());
    }

	public Long getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Long openTime) {
		this.openTime = openTime;
	}

	public BigDecimal getOpen() {
		return open;
	}

	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	public BigDecimal getLow() {
		return low;
	}

	public void setLow(BigDecimal low) {
		this.low = low;
	}

	public BigDecimal getClose() {
		return close;
	}

	public void setClose(BigDecimal close) {
		this.close = close;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public Long getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Long closeTime) {
		this.closeTime = closeTime;
	}

	public BigDecimal getQuoteAssetVolume() {
		return quoteAssetVolume;
	}

	public void setQuoteAssetVolume(BigDecimal quoteAssetVolume) {
		this.quoteAssetVolume = quoteAssetVolume;
	}

	public Long getNumberOfTrades() {
		return numberOfTrades;
	}

	public void setNumberOfTrades(Long numberOfTrades) {
		this.numberOfTrades = numberOfTrades;
	}

	public BigDecimal getTakerBuyBaseAssetVolume() {
		return takerBuyBaseAssetVolume;
	}

	public void setTakerBuyBaseAssetVolume(BigDecimal takerBuyBaseAssetVolume) {
		this.takerBuyBaseAssetVolume = takerBuyBaseAssetVolume;
	}

	public BigDecimal getTakerBuyQuoteAssetVolume() {
		return takerBuyQuoteAssetVolume;
	}

	public void setTakerBuyQuoteAssetVolume(BigDecimal takerBuyQuoteAssetVolume) {
		this.takerBuyQuoteAssetVolume = takerBuyQuoteAssetVolume;
	}
    
    
}
