package com.robindrew.trading.price.candle.generator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.robindrew.common.date.Dates;
import com.robindrew.common.lang.RandomElement;
import com.robindrew.trading.price.candle.IPriceCandle;
import com.robindrew.trading.price.candle.PriceCandle;
import com.robindrew.trading.price.candle.interval.IPriceCandleInterval;

public class PriceCandleGenerator {

	private final IPriceCandleInterval interval;
	private final RandomElement random;
	private int decimalPlaces = 2;

	public PriceCandleGenerator(IPriceCandleInterval interval, long randomSeed) {
		this.interval = interval;
		this.random = new RandomElement(randomSeed);
	}

	public PriceCandleGenerator(IPriceCandleInterval interval) {
		this(interval, interval.getLength());
	}

	public List<IPriceCandle> generateCandles(int count) {
		return generateCandles(count, LocalDateTime.now());
	}

	public List<IPriceCandle> generateCandles(int count, LocalDateTime from) {
		long openTime = Dates.toMillis(from);
		openTime = interval.getTimePeriod(openTime);

		int closePrice = 10000;

		List<IPriceCandle> list = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {

			IPriceCandle candle = generateCandle(openTime, closePrice);
			closePrice = candle.getClosePrice();
			openTime += interval.getLength();

			list.add(candle);
		}
		return list;
	}

	private IPriceCandle generateCandle(long openTime, int previousClosePrice) {
		long closeTime = openTime + interval.getLength();

		int openPrice = previousClosePrice;
		int highPrice = random.nextInt(openPrice + 1, openPrice + 100);
		int lowPrice = random.nextInt(openPrice - 100, openPrice - 1);
		int closePrice = random.nextInt(lowPrice, highPrice);

		return new PriceCandle(openPrice, highPrice, lowPrice, closePrice, openTime, closeTime, decimalPlaces);
	}

}