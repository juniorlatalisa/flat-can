package br.com.virtualsistemas.common.adapters;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import br.com.virtualsistemas.common.Constants;

public class LocalDateTimeXmlAdapter extends XmlAdapter<String, LocalDateTime> {

	@Override
	public LocalDateTime unmarshal(String value) throws Exception {
		return ((value == null) || (value.isEmpty())) ? null
				: LocalDateTime.parse(value, Constants.DATE_TIME_FORMATTER);
	}

	@Override
	public String marshal(LocalDateTime value) throws Exception {
		return (value == null) ? null : value.format(Constants.DATE_TIME_FORMATTER);
	}
}