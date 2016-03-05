package com.hitg.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

@Component
public class LocalDateTimeToLocalDateTimeConverter extends BidirectionalConverter<LocalDateTime, LocalDateTime> {
	@Override
	public LocalDateTime convertTo(final LocalDateTime source, final Type<LocalDateTime> destinationType) {
		return source;
	}

	@Override
	public LocalDateTime convertFrom(final LocalDateTime source, final Type<LocalDateTime> destinationType) {
		return source;
	}
}
