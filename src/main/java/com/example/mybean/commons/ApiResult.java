package com.example.mybean.commons;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ApiResult<T> {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long count;
	private final T data;

	public ApiResult(T data) {
		this(null, data);
	}
	public ApiResult(Long count, T data) {
		this.count = count;
		this.data = data;
	}

	public Long getCount() {
		return count;
	}

	public T getData() {
		return data;
	}
}
