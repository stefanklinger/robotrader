package org.robotrader.quote.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

@Entity
public class Quote {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
	private LocalDate date;

	@ManyToOne
	private Stock stock;

	private double open, close, high, low;

	private long volume;
	
	public Quote() {
		// for hibernate
	}
	
	public Quote(Stock stock, LocalDate date, double open, double close, double high, double low) {
		this.stock = stock;
		this.open = open;
		this.close = close;
		this.high = high;
		this.low = low;
		this.date = date;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	public long getVolume() {
		return volume;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getHigh() {
		return high;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getLow() {
		return low;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public double getClose() {
		return close;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getOpen() {
		return open;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public Stock getStock() {
		return stock;
	}

	public Long getId() {
		return id;
	}

	public int hashCode() {
		return new HashCodeBuilder().append(id).hashCode();
	}

	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}

		if (this == other) {
			return true;
		}

		if (!(other instanceof Quote)) {
			return false;
		}

		Quote that = (Quote) other;

		return new EqualsBuilder().append(date, that.date)
				.append(stock, that.stock).append(open, that.open)
				.append(close, that.close).append(high, that.high)
				.append(low, that.low).isEquals();
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
