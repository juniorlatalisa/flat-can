package br.com.virtualsistemas.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import br.com.virtualsistemas.common.Constants;
import br.com.virtualsistemas.common.builders.DateBuilder;

/**
 * @author juniorlatalisa
 */
public class DateUtils {

	private DateUtils() {}

	public static Calendar dateToCalendar(Date date) {
		GregorianCalendar calendar = new GregorianCalendar(Constants.TIME_ZONE, Constants.BRAZIL);
		calendar.setTime(date);
		return calendar;
	}

	public static LocalDateTime dateToLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), Constants.ZONE_OFFSET);
	}

	public static LocalDate dateToLocalDate(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), Constants.ZONE_OFFSET).toLocalDate();
	}

	public static LocalTime dateToLocalTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), Constants.ZONE_OFFSET).toLocalTime();
	}

	public static Date dateFrom(LocalDateTime value) {
		return Date.from(value.toInstant(Constants.ZONE_OFFSET));
	}

	public static Date dateFrom(LocalDate value) {
		return Date.from(value.atStartOfDay(Constants.ZONE_OFFSET).toInstant());
	}

	public static Date dateFrom(LocalTime value) {
		return Date.from(value.atDate(LocalDate.now()).toInstant(Constants.ZONE_OFFSET));
	}

	/**
	 * Obter uma parte da data conforme o campo informado.
	 * 
	 * @see Calendar#get(int)
	 */
	public static int getDatePart(Date date, int field) {
		return dateToCalendar(date).get(field);
	}

	/**
	 * Retorna o dia da semana onde domingo é 1
	 * 
	 * @see Calendar#SUNDAY
	 * @see Calendar#MONDAY
	 * @see Calendar#TUESDAY
	 * @see Calendar#WEDNESDAY
	 * @see Calendar#THURSDAY
	 * @see Calendar#FRIDAY
	 * @see Calendar#SATURDAY
	 * @see Calendar#DAY_OF_WEEK
	 */
	public static int getDayOfWeek(Date date) {
		return getDatePart(date, Calendar.DAY_OF_WEEK);
	}

	/**
	 * Retorna o dia no mês começando por 1
	 * 
	 * @see Calendar#DAY_OF_MONTH
	 */
	public static int getDay(Date date) {
		return getDatePart(date, Calendar.DAY_OF_MONTH);
	}

	/**
	 * Obter o valor inteiro referente ao ano.
	 * 
	 * @see Calendar#YEAR
	 */
	public static int getYear(Date date) {
		return getDatePart(date, Calendar.YEAR);
	}

	/**
	 * Obter o valor da hora no formado 24
	 * 
	 * @see Calendar#HOUR_OF_DAY
	 */
	public static int getHour(Date hour) {
		return getDatePart(hour, Calendar.HOUR_OF_DAY);
	}

	/**
	 * Obter o valor do mês onde janeiro é 0. É recomendado tratar o retorno palas
	 * constantes da classe Calendar
	 * 
	 * @see Calendar#MONTH
	 * @see Calendar#JANUARY
	 * @see Calendar#FEBRUARY
	 * @see Calendar#MARCH
	 * @see Calendar#APRIL
	 * @see Calendar#MAY
	 * @see Calendar#JULY
	 * @see Calendar#JUNE
	 * @see Calendar#AUGUST
	 * @see Calendar#SEPTEMBER
	 * @see Calendar#OCTOBER
	 * @see Calendar#NOVEMBER
	 * @see Calendar#DECEMBER
	 */
	public static int getMonth(Date date) {
		return getDatePart(date, Calendar.MONTH);
	}

	/**
	 * Obter o valor inteiro referente aos minutos.
	 * 
	 * @see Calendar#MINUTE
	 */
	public static int getMinute(Date minute) {
		return getDatePart(minute, Calendar.MINUTE);
	}

	/**
	 * Obter o valor inteiro referente aos segundos.
	 * 
	 * 
	 * @see Calendar#SECOND
	 */
	public static int getSecond(Date hora) {
		return getDatePart(hora, Calendar.SECOND);
	}

	/**
	 * Obtero valor em milisegundos, a partir da data informada.
	 * 
	 * 
	 * @see Calendar#MILLISECOND
	 */
	public static int getMillisecond(Date hora) {
		return getDatePart(hora, Calendar.MILLISECOND);
	}

	/**
	 * Retornar um valor em milissegudos de um determinado campo do calendario ou -1
	 * se o campo não for suportado. Campos suportados:
	 * 
	 * {@link Calendar#MILLISECOND}, {@link Calendar#SECOND},
	 * {@link Calendar#MINUTE}, {@link Calendar#HOUR}, {@link Calendar#HOUR_OF_DAY},
	 * {@link Calendar#DAY_OF_MONTH}
	 */
	public static long getMilliseconds(int field) {
		switch (field) {
		case Calendar.MILLISECOND:
			return 1;
		case Calendar.SECOND:
			return 1000;
		case Calendar.MINUTE:
			return 60000;
		case Calendar.HOUR:
			return 3600000;
		case Calendar.HOUR_OF_DAY:
			return 3600000;
		case Calendar.DAY_OF_MONTH:
			return 86400000;
		default:
			return -1L;
		}
	}

	/**
	 * Obtem o valor positivo da diferença entre as datas conforme o campo
	 * informado. Caso o campo não seja suportado o retorno será -1.
	 * 
	 */
	public static long dateDiff(int field, Date startDate, Date endDate) {
		if (!((startDate == null) || (endDate == null))) {
			if (// Diferença entre meses se o dia do mes for igual
			(Calendar.MONTH == field) && (getDay(startDate) == getDay(endDate))) {
				return (getYear(startDate) == getYear(endDate)) ? Math.abs(getMonth(endDate) - getMonth(startDate))
						: 12 - getMonth(startDate) + getMonth(endDate)
								+ (Math.abs(getYear(endDate) - getYear(startDate)) - 1) * 12;
			} else {
				long divisor = getMilliseconds(field);
				if (divisor > 0) {
					long msInicio = startDate.getTime();
					long msFim = endDate.getTime();

					msInicio -= msInicio % divisor;
					msFim -= msFim % divisor;

					return Math.abs(msFim - msInicio) / divisor;
				}
			}

			if (startDate.after(endDate)) {
				Date aux;
				aux = startDate;
				startDate = endDate;
				endDate = aux;
			}

			DateBuilder db = new DateBuilder();
			long retorno = -1;
			while (db.build().before(endDate)) {
				db.add(field, 1);
				retorno++;
			}
			return (retorno > 0) ? retorno : 0;
		}

		return -1;
	}

	/**
	 * Método genérico para comparar datas. Ele verifica se uma data informada
	 * refere-se a determinado dia da semana ou mes do ano.
	 * 
	 */
	protected static boolean is(Date date, int field, int get) {
		return getDatePart(date, field) == get;
	}

	// Métodos para comparção dos dias da semana.

	/**
	 * Retorna verdadeiro se o dia da semana referente a dara informada for Domingo.
	 * 
	 * @see Calendar#DAY_OF_WEEK
	 * @see Calendar#SUNDAY
	 */
	public static boolean isSunday(Date date) {
		return is(date, Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
	}

	/**
	 * Retorna verdadeiro se o dia da semana referente a dara informada for
	 * Segunda-feira
	 * 
	 * @see Calendar#DAY_OF_WEEK
	 * @see Calendar#MONDAY
	 */
	public static boolean isMonday(Date date) {
		return is(date, Calendar.DAY_OF_WEEK, Calendar.MONDAY);
	}

	/**
	 * Retorna verdadeiro se o dia da semana referente a dara informada for
	 * Terça-feira
	 * 
	 * @see Calendar#DAY_OF_WEEK
	 * @see Calendar#TUESDAY
	 */
	public static boolean isTuesday(Date date) {
		return is(date, Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
	}

	/**
	 * Retorna verdadeiro se o dia da semana referente a dara informada for
	 * Quarta-feira
	 * 
	 * @see Calendar#DAY_OF_WEEK
	 * @see Calendar#WEDNESDAY
	 */
	public static boolean isWednesday(Date date) {
		return is(date, Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
	}

	/**
	 * Retorna verdadeiro se o dia da semana referente a dara informada for
	 * Quinta-feira
	 * 
	 * @see Calendar#DAY_OF_WEEK
	 * @see Calendar#THURSDAY
	 */
	public static boolean isThursday(Date date) {
		return is(date, Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
	}

	/**
	 * Retorna verdadeiro se o dia da semana referente a dara informada for
	 * Sexta-feira
	 * 
	 * @see Calendar#DAY_OF_WEEK
	 * @see Calendar#FRIDAY
	 */
	public static boolean isFriday(Date date) {
		return is(date, Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
	}

	/**
	 * Retorna verdadeiro se o dia da semana referente a dara informada for Sábado.
	 * 
	 * @see Calendar#DAY_OF_WEEK
	 * @see Calendar#SATURDAY
	 */
	public static boolean isSaturday(Date date) {
		return is(date, Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
	}

	// Métodos para comparação dos meses.

	/**
	 * Retorna verdadeiro se o mês referente a dara informada for Janeiro.
	 * 
	 * @see Calendar#MONDAY
	 * @see Calendar#JANUARY
	 */
	public static boolean isJanuary(Date date) {
		return is(date, Calendar.MONTH, Calendar.JANUARY);
	}

	/**
	 * Retorna verdadeiro se o mês referente a dara informada for Fevereiro.
	 * 
	 * @see Calendar#MONDAY
	 * @see Calendar#FEBRUARY
	 */
	public static boolean isFebruary(Date date) {
		return is(date, Calendar.MONTH, Calendar.FEBRUARY);
	}

	/**
	 * Retorna verdadeiro se o mês referente a dara informada for Março.
	 * 
	 * @see Calendar#MONDAY
	 * @see Calendar#MARCH
	 */
	public static boolean isMarch(Date date) {
		return is(date, Calendar.MONTH, Calendar.MARCH);
	}

	/**
	 * Retorna verdadeiro se o mês referente a dara informada for Abril.
	 * 
	 * @see Calendar#MONDAY
	 * @see Calendar#APRIL
	 */
	public static boolean isApril(Date date) {
		return is(date, Calendar.MONTH, Calendar.APRIL);
	}

	/**
	 * Retorna verdadeiro se o mês referente a dara informada for Maio.
	 * 
	 * @see Calendar#MONDAY
	 * @see Calendar#MAY
	 */
	public static boolean isMay(Date date) {
		return is(date, Calendar.MONTH, Calendar.MAY);
	}

	/**
	 * Retorna verdadeiro se o mês referente a dara informada for Junho.
	 * 
	 * @see Calendar#MONDAY
	 * @see Calendar#JUNE
	 */
	public static boolean isJune(Date date) {
		return is(date, Calendar.MONTH, Calendar.JUNE);
	}

	/**
	 * Retorna verdadeiro se o mês referente a dara informada for Julho.
	 * 
	 * @see Calendar#MONDAY
	 * @see Calendar#JULY
	 */
	public static boolean isJuly(Date date) {
		return is(date, Calendar.MONTH, Calendar.JULY);
	}

	/**
	 * Retorna verdadeiro se o mês referente a dara informada for Agosto.
	 * 
	 * @see Calendar#MONDAY
	 * @see Calendar#AUGUST
	 */
	public static boolean isAugust(Date date) {
		return is(date, Calendar.MONTH, Calendar.AUGUST);
	}

	/**
	 * Retorna verdadeiro se o mês referente a dara informada for Setembro.
	 * 
	 * @see Calendar#MONDAY
	 * @see Calendar#SEPTEMBER
	 */
	public static boolean isSeptember(Date date) {
		return is(date, Calendar.MONTH, Calendar.SEPTEMBER);
	}

	/**
	 * Retorna verdadeiro se o mês referente a dara informada for Outubro.
	 * 
	 * @see Calendar#MONDAY
	 * @see Calendar#OCTOBER
	 */
	public static boolean isOctober(Date date) {
		return is(date, Calendar.MONTH, Calendar.OCTOBER);
	}

	/**
	 * Retorna verdadeiro se o mês referente a dara informada for Novembro.
	 * 
	 * @see Calendar#MONDAY
	 * @see Calendar#NOVEMBER
	 */
	public static boolean isNovember(Date date) {
		return is(date, Calendar.MONTH, Calendar.NOVEMBER);
	}

	/**
	 * Retorna verdadeiro se o mês referente a dara informada for Dezembro.
	 * 
	 * @see Calendar#MONDAY
	 * @see Calendar#DECEMBER
	 */
	public static boolean isDecember(Date date) {
		return is(date, Calendar.MONTH, Calendar.DECEMBER);
	}

	/**
	 * Verificar se o ano informado (valor inteiro) é um ano bissexto.
	 * 
	 * @see GregorianCalendar
	 * @see GregorianCalendar#isLeapYear(int)
	 */
	public static boolean isLeapYear(int year) {
		return (new GregorianCalendar(Constants.TIME_ZONE, Constants.BRAZIL)).isLeapYear(year);
	}

	/**
	 * Verificar se a data informada representa um ano bissexto.
	 * 
	 * @see #isLeapYear(int)
	 */
	public static boolean isLeapYear(Date date) {
		return isLeapYear(getYear(date));
	}

	/**
	 * Compara se duas datas são iguais independente da hora
	 * 
	 */
	public static boolean isSameDay(Date date1, Date date2) {
		Calendar calendar1 = dateToCalendar(date1);
		Calendar calendar2 = dateToCalendar(date2);
		return ((calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)) && //
				(calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)) && //
				(calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)));
	}
}