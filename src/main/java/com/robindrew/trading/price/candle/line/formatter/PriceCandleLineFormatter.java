package com.robindrew.trading.price.candle.line.formatter;

import static com.robindrew.common.date.Dates.toLocalDateTime;

import com.robindrew.trading.price.candle.IPriceCandle;

public class PriceCandleLineFormatter implements IPriceCandleLineFormatter {

	private static final String DEFAULT_EOL = "\n";

	private String endOfLine = DEFAULT_EOL;

	@Override
	public String formatCandle(IPriceCandle candle, boolean includeEndOfLine) {

		StringBuilder line = new StringBuilder();
		line.append(toLocalDateTime(candle.getOpenTime())).append(',');
		line.append(toLocalDateTime(candle.getCloseTime())).append(',');
		line.append(candle.getMidOpenPrice()).append(',');
		line.append(candle.getMidHighPrice()).append(',');
		line.append(candle.getMidLowPrice()).append(',');
		line.append(candle.getMidClosePrice());

		// End of line?
		if (includeEndOfLine) {
			line.append(getEndOfLine());
		}

		return line.toString();
	}

	public String getEndOfLine() {
		return endOfLine;
	}

	public void setEndOfLine(String endOfLine) {
		if (endOfLine.isEmpty()) {
			throw new IllegalArgumentException("endOfLine is empty");
		}
		this.endOfLine = endOfLine;
	}

}
