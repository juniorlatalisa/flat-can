package br.com.virtualsistemas.common.adapters;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import br.com.virtualsistemas.common.Constants;

public class DateTimeXmlAdapter extends XmlAdapter<String, Date> {

	@Override
	public Date unmarshal(String value) throws Exception {
		return ((value == null) || (value.isEmpty())) ? null : Constants.DATE_TIME_FORMAT.parse(value) ;
	}

	@Override
	public String marshal(Date value) throws Exception {
		return (value == null) ? null : Constants.DATE_TIME_FORMAT.format(value);
	}
}