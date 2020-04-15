package br.com.virtualsistemas.common.adapters;

import java.time.LocalTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import br.com.virtualsistemas.common.Constants;

public class LocalTimeXmlAdapter extends XmlAdapter<String, LocalTime> {

	@Override
	public LocalTime unmarshal(String value) throws Exception {
		return ((value == null) || (value.isEmpty())) ? null : LocalTime.parse(value, Constants.TIME_FORMATTER);
	}

	@Override
	public String marshal(LocalTime value) throws Exception {
		return (value == null) ? null : value.format(Constants.TIME_FORMATTER);
	}
}