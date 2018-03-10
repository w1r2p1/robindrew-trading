package com.robindrew.trading.price.candle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.robindrew.trading.price.candle.checker.PriceCandleSortedChecker;
import com.robindrew.trading.price.candle.filter.PriceCandleDateFilter;
import com.robindrew.trading.price.candle.filter.PriceCandleDateTimeFilter;
import com.robindrew.trading.price.candle.interval.TimeUnitInterval;
import com.robindrew.trading.price.candle.io.list.source.IPriceCandleListSource;
import com.robindrew.trading.price.candle.io.stream.source.IPriceCandleStreamSource;
import com.robindrew.trading.price.candle.io.stream.source.PriceCandleCheckerStreamSource;
import com.robindrew.trading.price.candle.io.stream.source.PriceCandleFilteredStreamSource;
import com.robindrew.trading.price.candle.io.stream.source.PriceCandleIntervalStreamSource;
import com.robindrew.trading.price.candle.io.stream.source.PriceCandleStreamToListSource;
import com.robindrew.trading.price.candle.io.stream.source.iterator.PriceCandleStreamSourceIterable;
import com.robindrew.trading.price.candle.io.stream.source.iterator.PriceCandleStreamSourceIterator;
import com.robindrew.trading.price.candle.merger.PriceCandleMerger;
import com.robindrew.trading.price.close.IClosePrice;

public class PriceCandles {

	public static IPriceCandle merge(IPriceCandle candle1, IPriceCandle candle2) {
		return new PriceCandleMerger().merge(candle1, candle2);
	}

	public static IPriceCandle merge(Collection<? extends IPriceCandle> candles) {
		return new PriceCandleMerger().merge(candles);
	}

	public static double getAverage(double total, int count) {
		return (count <= 1) ? total : (total / count);
	}

	public static double getSmoothingConstant(int periods) {
		if (periods < 1) {
			throw new IllegalArgumentException("periods=" + periods);
		}
		return 2.0f / (periods + 1);
	}

	public static int getChange(IClosePrice previous, IClosePrice current) {
		return current.getClosePrice() - previous.getClosePrice();
	}

	public static double getPercentDifference(double price1, double price2) {
		double diff = (price1 > price2) ? price1 / price2 : price2 / price1;
		// Convert to a percentage ...
		return (diff * 100.0) - 100.0;
	}

	public static Iterator<IPriceCandle> iterator(IPriceCandleStreamSource source) {
		return new PriceCandleStreamSourceIterator(source);
	}

	public static Iterable<IPriceCandle> iterable(IPriceCandleStreamSource source) {
		return new PriceCandleStreamSourceIterable(source);
	}

	public static final List<IPriceCandle> drainToList(IPriceCandleStreamSource source) {
		try (IPriceCandleListSource list = new PriceCandleStreamToListSource(source)) {
			return list.getNextCandles();
		}
	}

	public static IPriceCandleStreamSource filterByDates(IPriceCandleStreamSource source, LocalDateTime from, LocalDateTime to) {
		return new PriceCandleFilteredStreamSource(source, new PriceCandleDateTimeFilter(from, to));
	}

	public static IPriceCandleStreamSource filterByDates(IPriceCandleStreamSource source, LocalDate from, LocalDate to) {
		return new PriceCandleFilteredStreamSource(source, new PriceCandleDateFilter(from, to));
	}

	public static IPriceCandleStreamSource checkSorted(IPriceCandleStreamSource source) {
		return new PriceCandleCheckerStreamSource(source, new PriceCandleSortedChecker());
	}

	public static IPriceCandleStreamSource aggregate(IPriceCandleStreamSource source, long interval, TimeUnit unit) {
		return new PriceCandleIntervalStreamSource(source, new TimeUnitInterval(interval, unit));
	}

}
