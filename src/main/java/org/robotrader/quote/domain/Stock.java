package org.robotrader.quote.domain;

import javax.persistence.Entity;

@Entity
public class Stock {

	private String code;

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
