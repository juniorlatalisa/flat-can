package br.com.virtualsistemas.common.adapters;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import br.com.virtualsistemas.common.Constants;

public class LocalDateXmlAdapter extends XmlAdapter<String, LocalDate> {

	@Override
	public LocalDate unmarshal(String value) throws Exception {
		return ((value == null) || (value.isEmpty())) ? null
				: LocalDate.parse(value, Constants.DATE_FORMATTER);
	}

	@Override
	public String marshal(LocalDate value) throws Exception {
		return (value == null) ? null : value.format(Constants.DATE_FORMATTER);
	}
}