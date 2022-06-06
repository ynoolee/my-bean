package com.example.mybean.order.model;

import java.util.Objects;
import java.util.regex.Pattern;

import org.springframework.util.Assert;

public class Email {
	private final String address;
	private final static Pattern pattern = Pattern.compile("\\b[\\w.-]+@[\\w.-]+\\.\\w{2,4}\\b");

	private Email(String email) {
		Assert.notNull(email, "email 은 null 이 올 수 없습니다");
		Assert.isTrue(email.length() >= 4 && email.length() <= 50, "email 길이는 4 ~ 50 글자여야 합니다");
		Assert.isTrue(checkAddress(email), "유효하지 않은 email 입니다");

		this.address = email;
	}

	public static Email of(String email) {
		return new Email(email);
	}

	private boolean checkAddress(String email) {
		return pattern.matcher(email)
			.matches();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Email email1 = (Email)o;

		return Objects.equals(address, email1.address);
	}

	@Override
	public int hashCode() {
		return Objects.hash(address);
	}

	@Override
	public String toString() {
		return "Email{" +
			"email='" + address + '\'' +
			'}';
	}

	public String getAddress() {
		return address;
	}
}