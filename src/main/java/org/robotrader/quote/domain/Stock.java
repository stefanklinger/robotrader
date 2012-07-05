package org.robotrader.quote.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
public class Stock {

	@Id
	private String code;
	
	Stock() {
		// for hibernate
	}

	public Stock(String code) {
		this.code = code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}

		if (this == other) {
			return true;
		}

		if (!(other instanceof Stock)) {
			return false;
		}

		return StringUtils.equals(code, ((Stock) other).getCode());
	}
	
	public int hashCode() {
		return new HashCodeBuilder().append(code).toHashCode();
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
