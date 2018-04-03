package com.robindrew.trading.price.tick;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PriceTickDateComparator implements Comparator<IPriceTick> {

	private final boolean ascending;

	public PriceTickDateComparator(boolean ascending) {
		this.ascending = ascending;
	}

	public PriceTickDateComparator() {
		this(true);
	}

	@Override
	public int compare(IPriceTick tick1, IPriceTick tick2) {
		if (tick1.getTimestamp() <= tick2.getTimestamp()) {
			return ascending ? -1 : 1;
		}
		if (tick1.equals(tick2)) {
			return 0;
		}
		throw new IllegalArgumentException("Non equal, matching ticks, tick1=" + tick1 + ", tick2=" + tick2);
	}

	public void sort(List<IPriceTick> list) {
		Collections.sort(list, this);
	}

}