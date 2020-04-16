package br.com.virtualsistemas.common.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import br.com.virtualsistemas.common.Constants;
import br.com.virtualsistemas.common.builders.MapBuilder;

/**
 * @author juniorlatalisa
 */
public class StringUtils {
	
	private StringUtils() {}

	private static final Map<String, DecimalFormat> decimalFormat = new HashMap<>();
	private static final Map<String, SimpleDateFormat> simpleDateFormat = MapBuilder.build(Constants.DATE_TIME_PATTERN,
			Constants.DATE_TIME_FORMAT, Constants.DATE_PATTERN, Constants.DATE_FORMAT, Constants.TIME_PATTERN,
			Constants.TIME_FORMAT);

	private static DecimalFormat getDecimalFormat(String pattern, Locale locale) {
		if (!Constants.BRAZIL.equals(locale)) {
			return new DecimalFormat(pattern, DecimalFormatSymbols.getInstance(locale));
		}
		DecimalFormat retorno;
		synchronized (decimalFormat) {
			if ((retorno = decimalFormat.get(pattern)) == null) {
				decimalFormat.put(pattern, retorno = new DecimalFormat(pattern, Constants.DECIMAL_FORMAT_SYMBOLS));
			}
		}
		return retorno;
	}

	private static SimpleDateFormat getSimpleDateFormat(String pattern, Locale locale) {
		if (!Constants.BRAZIL.equals(locale)) {
			return new SimpleDateFormat(pattern, locale);
		}
		SimpleDateFormat retorno;
		synchronized (simpleDateFormat) {
			if ((retorno = simpleDateFormat.get(pattern)) == null) {
				simpleDateFormat.put(pattern, retorno = new SimpleDateFormat(pattern, locale));
			}
		}
		return retorno;
	}

	public static String formatNumber(Number number, String pattern, Locale locale) {
		return getDecimalFormat(pattern, locale).format(number);
	}

	public static String formatNumber(Number number, String pattern) {
		return formatNumber(number, pattern, Constants.BRAZIL);
	}

	public static String formatCurrency(Number number, Locale locale) {
		return NumberFormat.getCurrencyInstance(locale).format(number);
	}

	public static String formatCurrency(Number number) {
		return formatCurrency(number, Constants.BRAZIL);
	}

	public static String formatDate(Date date, String pattern, Locale locale) {
		return getSimpleDateFormat(pattern, locale).format(date);
	}

	public static String formatDate(Date date, String pattern) {
		return formatDate(date, pattern, Constants.BRAZIL);
	}

	/**
	 * @see DateFormat
	 * @see DateFormat#getDateInstance(int, Locale)
	 * @see DateFormat#format(Date)
	 * @see DateFormat#FULL
	 * @see DateFormat#LONG
	 * @see DateFormat#MEDIUM
	 * @see DateFormat#SHORT
	 */
	public static String formatDate(Date date, int style, Locale locale) {
		return DateFormat.getDateInstance(style, locale).format(date);
	}

	public static String formatDate(Date date, int style) {
		return formatDate(date, style, Constants.BRAZIL);
	}

	public static String formatDate(Date date) {
		return formatDate(date, DateFormat.MEDIUM, Constants.BRAZIL);
	}

	/**
	 * @see DateFormat
	 * @see DateFormat#getTimeInstance(int, Locale)
	 * @see DateFormat#format(Date)
	 * @see DateFormat#FULL
	 * @see DateFormat#LONG
	 * @see DateFormat#MEDIUM
	 * @see DateFormat#SHORT
	 */
	public static String formatTime(Date date, int style, Locale locale) {
		return DateFormat.getTimeInstance(style, locale).format(date);
	}

	public static String formatTime(Date date, int style) {
		return formatTime(date, style, Constants.BRAZIL);
	}

	public static String formatTime(Date date) {
		return formatTime(date, DateFormat.MEDIUM, Constants.BRAZIL);
	}

	/**
	 * @see DateFormat
	 * @see DateFormat#getDateTimeInstance(int, int, Locale)
	 * @see DateFormat#format(Date)
	 * @see DateFormat#FULL
	 * @see DateFormat#LONG
	 * @see DateFormat#MEDIUM
	 * @see DateFormat#SHORT
	 */
	public static String formatDateTime(Date date, int dateStyle, int timeStyle, Locale locale) {
		return DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale).format(date);
	}

	public static String formatDateTime(Date date, int dateStyle, int timeStyle) {
		return formatDateTime(date, dateStyle, timeStyle, Constants.BRAZIL);
	}

	public static String formatDateTime(Date date, int style, Locale locale) {
		return formatDateTime(date, style, style, Constants.BRAZIL);
	}

	public static String formatDateTime(Date date, int style) {
		return formatDateTime(date, style, style, Constants.BRAZIL);
	}

	public static String formatDateTime(Date date) {
		return formatDateTime(date, DateFormat.MEDIUM, DateFormat.MEDIUM, Constants.BRAZIL);
	}

	/*
	 * public static <T> T stringToSafeValue(Supplier<T> supplier, T defaultValue) {
	 * try { return supplier.get(); } catch (Throwable e) { return defaultValue; } }
	 */

	public static int stringToInteger(String value, int defaultValue) {
		try {
			return Integer.parseInt(value, 10);
		} catch (Throwable e) {
			return defaultValue;
		}
	}

	public static long stringToLong(String value, long defaultValue) {
		try {
			return Long.parseLong(value, 10);
		} catch (Throwable e) {
			return defaultValue;
		}
	}
	
	public static double currencyToDouble(String value, Locale locale) {
		try {
			return NumberFormat.getCurrencyInstance(locale).parse(value).doubleValue();
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static double currencyToDouble(String value) {
		return currencyToDouble(value, Constants.BRAZIL);
	}

	public static double currencyToDouble(String value, Locale locale, double defaultValue) {
		try {
			return NumberFormat.getCurrencyInstance(locale).parse(value).doubleValue();
		} catch (ParseException e) {
			return defaultValue;
		}
	}

	public static double currencyToDouble(String value, double defaultValue) {
		return currencyToDouble(value, Constants.BRAZIL, defaultValue);
	}

	public static double stringToDouble(String value, double defaultValue) {
		try {
			return Double.parseDouble(value);
		} catch (Throwable e) {
			return defaultValue;
		}
	}

	public static Date stringToDate(Supplier<Date> supplier, Date defaultValue) {
		try {
			return supplier.get();
		} catch (Throwable e) {
			return defaultValue;
		}
	}

	public static Date stringToDate(String value, String pattern, Locale locale) {
		try {
			return getSimpleDateFormat(pattern, locale).parse(value);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static Date stringToDate(String value, String pattern) {
		return stringToDate(value, pattern, Constants.BRAZIL);
	}

	public static Date stringToDate(String value, int style, Locale locale) {
		try {
			return DateFormat.getDateInstance(style, locale).parse(value);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static Date stringToDate(String date, int style) {
		return stringToDate(date, style, Constants.BRAZIL);
	}

	public static Date stringToTime(String value, int style, Locale locale) {
		try {
			return DateFormat.getTimeInstance(style, locale).parse(value);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static Date stringToTime(String time, int style) {
		return stringToTime(time, style, Constants.BRAZIL);
	}

	/**
	 * @see ParseException
	 */
	public static Date stringToDateTime(String value, int dateStyle, int timeStyle, Locale locale) {
		try {
			return DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale).parse(value);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static Date stringToDateTime(String date, int dateStyle, int timeStyle) {
		return stringToDateTime(date, dateStyle, timeStyle, Constants.BRAZIL);
	}

	public static Date stringToDateTime(String date, int style) {
		return stringToDateTime(date, style, style, Constants.BRAZIL);
	}

	public static Date stringToDateTime(String date) {
		return stringToDateTime(date, DateFormat.MEDIUM, DateFormat.MEDIUM, Constants.BRAZIL);
	}

	public static boolean matches(String regex, CharSequence input) {
		return Pattern.compile(regex).matcher(input).matches();
	}

	public static String encodeHEX(byte[] value) {
		return new BigInteger(1, value).toString(16);
	}

	public static byte[] decodeHEX(String value) {
		return new BigInteger(value, 16).toByteArray();
	}

	/**
	 * @see URLEncoder#encode(String, String)
	 */
	public static String encodeURL(String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @see URLDecoder#decode(String, String)
	 */
	public static String decodeURL(String value) {
		try {
			return URLDecoder.decode(value, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @see Encoder#encode(byte[])
	 */
	public static byte[] encodeBase64(String value) {
		return Base64.getEncoder().encode(value.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * @see Decoder#decode(byte[])
	 */
	public static String decodeBase64(byte[] value) {
		return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
	}

	/**
	 * @see Jsonb#toJson(Object)
	 */
	public static String encodeJSON(Object value) {
		return getJsonb().toJson(value);
	}

	/**
	 * @see Jsonb#fromJson(String, Class)
	 */
	public static <T> T decodeJSON(String value, Class<T> type) {
		return getJsonb().fromJson(value, type);
	}

	private static Jsonb jsonb = null;

	private static Jsonb getJsonb() {
		if (jsonb == null) {
			jsonb = JsonbBuilder.create();
		}
		return jsonb;
	}

}