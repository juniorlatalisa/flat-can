package br.com.virtualsistemas.common.builders;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import br.com.virtualsistemas.common.Constants;
import br.com.virtualsistemas.common.utils.DateUtils;

public class DateBuilder {

	private Calendar source;

	protected DateBuilder(Calendar calendar) {
		this.source = calendar;
	}

	public DateBuilder() {
		this(new GregorianCalendar(Constants.TIME_ZONE, Constants.BRAZIL));
	}

	public Date build() {
		return source.getTime();
	}

	/**
	 * Configurar os campos do calendário.
	 * 
	 * @see Calendar#set(int, int)
	 */
	public DateBuilder setField(int field, int value) {
		source.set(field, value);
		return this;
	}

	/**
	 * Configurar o campo dia do mes no Calendario.
	 * 
	 * @see Calendar#DAY_OF_MONTH
	 */
	public DateBuilder setDay(int day) {
		return setField(Calendar.DAY_OF_MONTH, day);
	}

	/**
	 * Confgurar o campo mes no Calendario.
	 * 
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
	 * @see Calendar#MONTH
	 */
	public DateBuilder setMonth(int month) {
		return setField(Calendar.MONTH, month);
	}

	/**
	 * Configurar o campo ano no Calendario.
	 * 
	 * @see Calendar#YEAR
	 */
	public DateBuilder setYear(int year) {
		return setField(Calendar.YEAR, year);
	}

	/**
	 * Configurar o campo hora no calendario.
	 * 
	 * @see Calendar#HOUR_OF_DAY
	 */
	public DateBuilder setHour(int hour) {
		return setField(Calendar.HOUR_OF_DAY, hour);
	}

	/**
	 * Configurar o campo minuto no calendario.
	 * 
	 * @see Calendar#MINUTE
	 */
	public DateBuilder setMinute(int minute) {
		return setField(Calendar.MINUTE, minute);
	}

	/**
	 * Configurar o campo segundos no calendario.
	 * 
	 * @see Calendar#SECOND
	 */
	public DateBuilder setSecond(int second) {
		return setField(Calendar.SECOND, second);
	}

	/**
	 * Configurar o construtor do calendario passando o valor em milisegundos.
	 * 
	 * @see Calendar#MILLISECOND
	 */
	public DateBuilder setMillisecond(int millisecond) {
		return setField(Calendar.MILLISECOND, millisecond);
	}

	/**
	 * Configura o Fuso Horário.
	 * 
	 * @see Calendar#setTimeZone(TimeZone)
	 */
	public DateBuilder setTimeZone(TimeZone timezone) {
		source.setTimeZone(timezone);
		return this;
	}

	/**
	 * "Limpar" determinado campo.
	 * 
	 * @see Calendar#clear(int)
	 */
	public DateBuilder clear(int field) {
		source.clear(field);
		return this;
	}

	/**
	 * Configura a hora para 0 (zero)
	 * 
	 */
	public DateBuilder clearHour() {
		return setHour(0);
	}

	/**
	 * Configurar o campo dia do mês com o valor padrão.
	 * 
	 */
	public DateBuilder clearDay() {
		return clear(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Configurar o campo mês com valor padrão.
	 * 
	 */
	public DateBuilder clearMonth() {
		return clear(Calendar.MONTH);
	}

	/**
	 * Configurar o campo ano com valor padrão.
	 * 
	 */
	public DateBuilder clearYear() {
		return clear(Calendar.YEAR);
	}

	/**
	 * Configuar o campo minuto com o valor padrão.
	 * 
	 */
	public DateBuilder clearMinute() {
		return clear(Calendar.MINUTE);
	}

	/**
	 * Configuar o campo segundo com valor padrão
	 * 
	 */
	public DateBuilder clearSecond() {
		return clear(Calendar.SECOND);
	}

	/**
	 * Configuar o campo milissegundos com valor padrão.
	 * 
	 */
	public DateBuilder clearMillisecond() {
		return clear(Calendar.MILLISECOND);
	}

	/**
	 * Adicionar campos ao calendario este método recebe como argumento o campo e o
	 * valor.
	 * 
	 * @see Calendar#add(int, int)
	 */
	public DateBuilder add(int field, int amount) {
		source.add(field, amount);
		return this;
	}

	/**
	 * Adicionar milisegundos à data passando como parametro um valor em segundos.
	 * 
	 */
	public DateBuilder addMillisecond(int millisecond) {
		return add(Calendar.MILLISECOND, millisecond);
	}

	/**
	 * Adicionar 1 milissegundo a data.
	 * 
	 */
	public DateBuilder addMillisecond() {
		return addMillisecond(1);
	}

	/**
	 * Adicionar segundos ao calendario, o valor é um inteiro passado por parametro.
	 * 
	 */
	public DateBuilder addSecond(int seconds) {
		return add(Calendar.SECOND, seconds);
	}

	/**
	 * Adicionar 1 segundo ao calendario.
	 * 
	 */
	public DateBuilder addSecond() {
		return addSecond(1);
	}

	/**
	 * Adicionar minutos ao calendario, o valor é um inteiro passado por parametro.
	 * 
	 */
	public DateBuilder addMinute(int minutes) {
		return add(Calendar.MINUTE, minutes);
	}

	/**
	 * Adicioanar 1 minuto ao calendario.
	 * 
	 */
	public DateBuilder addMinute() {
		return addMinute(1);
	}

	/**
	 * Adicionar horas ao calendario, o valor de horas a ser adicionado é um inteiro
	 * passado por paramentro.
	 * 
	 */
	public DateBuilder addHour(int hours) {
		return add(Calendar.HOUR_OF_DAY, hours);
	}

	/**
	 * Adicionar uma hora ao calendario.
	 * 
	 */
	public DateBuilder addHour() {
		return addHour(1);
	}

	/**
	 * Adicioanar dias ao calendarrio, o valor de dias é um inteiro passado por
	 * parametro.
	 * 
	 */
	public DateBuilder addDay(int days) {
		return add(Calendar.DAY_OF_MONTH, days);
	}

	/**
	 * Adicionar 1 dia ao calendario.
	 * 
	 */
	public DateBuilder addDay() {
		return addDay(1);
	}

	/**
	 * Adicionar meses ao calendario, a quantidade de meses a adicionar é um valor
	 * inteiro passado por parametro.
	 * 
	 */
	public DateBuilder addMonth(int months) {
		return add(Calendar.MONTH, months);
	}

	/**
	 * Adicionar 1 mes ao calendario.
	 * 
	 */
	public DateBuilder addMonth() {
		return addMonth(1);
	}

	/**
	 * Adicionar anos ao calendario, passando como parametro um inteiro referente a
	 * quantidade de anos .
	 * 
	 */
	public DateBuilder addYear(int years) {
		return add(Calendar.YEAR, years);
	}

	/**
	 * Adcionar 1 ano ao calendario.
	 * 
	 */
	public DateBuilder addYear() {
		return addYear(1);
	}

	/**
	 * Configurar o calendario passando como paramentro o ano, o mes e um valor
	 * inteiro representando a data.
	 * 
	 * @see Calendar#set(int, int, int)
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
	public DateBuilder set(int year, int month, int date) {
		source.set(year, month, date);
		return this;
	}

	/**
	 * Metodo set acrescentando dois parametros, neste caso a hora e os minutos.
	 * 
	 * @see Calendar#set(int, int, int, int, int)
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
	public DateBuilder set(int year, int month, int date, int hourOfDay, int minute) {
		source.set(year, month, date, hourOfDay, minute);
		return this;
	}

	/**
	 * Sobrecarrega o metodo set acrescentando dois parametros, neste caso a hora,
	 * os minutos e os sgundos.
	 * 
	 * @see Calendar#set(int, int, int, int, int, int)
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
	public DateBuilder set(int year, int month, int date, int hourOfDay, int minute, int second) {
		source.set(year, month, date, hourOfDay, minute, second);
		return this;
	}

	/**
	 * Define uma nova base para o objeto.
	 * 
	 */
	public DateBuilder setCalendar(Calendar calendar) {
		this.source = calendar;
		return this;
	}

	/**
	 * Define uma nova base para o objeto.
	 * 
	 */
	public DateBuilder setDateTime(Date date) {
		source.setTime(date);
		return this;
	}

	/**
	 * Atribui somente a data a base do objeto mantendo a hora.
	 * 
	 */
	public DateBuilder setDateOnly(Date date) {
		Calendar calendar = DateUtils.dateToCalendar(date);
		return this//
				.setDay(calendar.get(Calendar.DAY_OF_MONTH))//
				.setMonth(calendar.get(Calendar.MONTH))//
				.setYear(calendar.get(Calendar.YEAR))//
		;
	}

	/**
	 * Atribui somente a hora a base do objeto mantendo a data.
	 * 
	 */
	public DateBuilder setTimeOnly(Date date) {
		Calendar calendar = DateUtils.dateToCalendar(date);
		return this//
				.setMillisecond(calendar.get(Calendar.MILLISECOND))//
				.setSecond(calendar.get(Calendar.SECOND))//
				.setMinute(calendar.get(Calendar.MINUTE))//
				.setHour(calendar.get(Calendar.HOUR_OF_DAY))//
		;
	}

	/**
	 * Atribui a hora 23:59:59.999 a base do objeto.
	 * 
	 * 
	 */
	public DateBuilder setEndOfDay() {
		return setHour(23).setMinute(59).setSecond(59).setMillisecond(999);
	}

	/**
	 * Atribui a hora 00:00:00.000 a base do objeto.
	 * 
	 * 
	 */
	public DateBuilder setStartOfDay() {
		return setHour(0).setMinute(0).setSecond(0).setMillisecond(0);
	}

	@Override
	public String toString() {
		return source.toString();
	}

	@Override
	public int hashCode() {
		return source.hashCode();
	}
}