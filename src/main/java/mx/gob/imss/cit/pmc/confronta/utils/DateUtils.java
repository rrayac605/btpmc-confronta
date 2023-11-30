package mx.gob.imss.cit.pmc.confronta.utils;

import mx.gob.imss.cit.pmc.confronta.constants.ConfrontaConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    public static String getFileFormattedDate(Date date) {
        TimeZone tz = TimeZone.getTimeZone("America/Mexico_City");
        DateFormat df = new SimpleDateFormat(ConfrontaConstants.PATTERN_YYYYMMDD);
        df.setTimeZone(tz);
        return df.format(date);
    }
    
    public static String getFileFormattedDateChanges(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat(ConfrontaConstants.PATTERN_YYYYMMDD);
        df.setTimeZone(tz);
        return df.format(date);
    }

    public static String getFileFormattedHour(Date date) {
        TimeZone tz = TimeZone.getTimeZone("America/Mexico_City");
        DateFormat df = new SimpleDateFormat(ConfrontaConstants.PATTERN_TIME);
        df.setTimeZone(tz);
        return df.format(date);
    }

    public static String getCurrentMexicoDateString() {
        TimeZone tz = TimeZone.getTimeZone("America/Mexico_City");
        DateFormat df = new SimpleDateFormat(ConfrontaConstants.PATTERN_DDMMYYYY_TIME);
        df.setTimeZone(tz);
        return df.format(new Date());
    }
    
    public static String obtenerDiaEjecucion(Date date) {
    	TimeZone tz = TimeZone.getTimeZone("America/Mexico_City");
        DateFormat df = new SimpleDateFormat(ConfrontaConstants.FORMATO_DIA_EJECUCION);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
    	calendar.setTimeZone(tz);
    	
    	int horaDelDia = calendar.get(Calendar.HOUR_OF_DAY);
    	
    	logger.info("Valor de la hora {}", horaDelDia);
        
        if(horaDelDia != 23) {
        	logger.info("Ya paso el dia de ejecucion");
        	calendar.add(Calendar.DAY_OF_YEAR, -1);
        	Date dt = calendar.getTime();
        	dt = calendar.getTime();
        	
        	return df.format(dt);
        } else {
        	logger.info("Aun no pasa el dia de ejecucion");
        	df.setTimeZone(tz);
        	return df.format(new Date());
        }
    
    }
    
    public static String obtenerDiaFin() {
    	TimeZone tz = TimeZone.getTimeZone("America/Mexico_City");
        DateFormat df = new SimpleDateFormat(ConfrontaConstants.FORMATO_DIA_EJECUCION);
        
        df.setTimeZone(tz);
        
        return df.format(new Date());
    }
    
    public static String obtenerHoraFin() {
    	TimeZone tz = TimeZone.getTimeZone("America/Mexico_City");
        DateFormat df = new SimpleDateFormat(ConfrontaConstants.FORMATO_HORA_EJEC);
        df.setTimeZone(tz);
        return df.format(new Date());
    }
    
    public static String obtenerDiaSig(Date date) {
    	Calendar calendar = Calendar.getInstance();
    	TimeZone tz = TimeZone.getTimeZone("America/Mexico_City");
        DateFormat df = new SimpleDateFormat(ConfrontaConstants.FORMATO_DIA_EJECUCION);
        
        calendar.setTime(date);
    	calendar.setTimeZone(tz);
    	
    	int horaDelDia = calendar.get(Calendar.HOUR_OF_DAY);
    	
    	logger.info("Valor de la hora {}", horaDelDia);
        
        if(horaDelDia >= 0) {
        	logger.info("Ya estoy en el dia siguiente");
        	calendar.get(Calendar.DAY_OF_YEAR);
        	Date dt = calendar.getTime();
        	dt = calendar.getTime();
        	
        	return df.format(dt);
        } else {
        	logger.info("Aun no llego al dia siguiente pero le voy a sumar 1");
        	calendar.add(Calendar.DAY_OF_YEAR, +1);
        	Date dt = calendar.getTime();
        	dt = calendar.getTime();
        	
        	return df.format(dt);
        }
    }
    
    public static String getCurrentMongoDateString(boolean isReprocess) {
        TimeZone tz = TimeZone.getTimeZone("America/Mexico_City");
        DateFormat df = new SimpleDateFormat(ConfrontaConstants.PATTERN_YYYY_MM_DD_TIME);

        df.setTimeZone(tz);
        Date currentDate = new Date();
        if (isReprocess) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            currentDate = calendar.getTime();
        }
        return df.format(currentDate).concat("T23:00:00CDT");
    }

    public static Date getCurrentMexicoDate() {
        try {
            return new SimpleDateFormat(ConfrontaConstants.PATTERN_DDMMYYYY_TIME).parse(getCurrentMexicoDateString());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCurrentYear() {
        String actualDate = getCurrentMexicoDateString();
        return actualDate.substring(4, 8);
    }

    public static String getCurrentYY() {
        String actualDate = getCurrentMexicoDateString();
        return actualDate.substring(6, 8);
    }

    private static String getFormattedDay(String day, String lastDayOfMonth) {
        return day != null && !day.equals("null") ? Integer.parseInt(day) < 10 ? ConfrontaConstants.ZERO_STRING.concat(day) : day
                : lastDayOfMonth != null ? lastDayOfMonth : ConfrontaConstants.FIRST_DAY;
    }

    private static String getFormattedMonth(String month) {
        return month.length() < 2 ? ConfrontaConstants.ZERO_STRING.concat(month) : month;
    }

    /**
     * Utileria que genera la fecha con las horas adecuadas para realizar la
     * consulta de movimientos, se agregan 6 horas a esta fecha dado que al momento
     * de realizar la busqueda mongodb agrega 6 horas y con estas se compenza la
     * busqueda de la misma
     */
    public static Date calculateBeginDate(String year, String month, String day) {
        String stringDate = getFormattedDay(day, null).concat(getFormattedMonth(month)).concat(year)
                .concat(ConfrontaConstants.BEGIN_HOURS);
        TimeZone tz = TimeZone.getTimeZone(ConfrontaConstants.ISO_TIMEZONE);
        DateFormat df = new SimpleDateFormat(ConfrontaConstants.PATTERN_DDMMYYYY_TIME);
        df.setTimeZone(tz);
        try {
            Date formattedDate = df.parse(stringDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(formattedDate);
            calendar.add(Calendar.HOUR, ConfrontaConstants.HOURS_TO_ADD);
            return calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Utileria que genera la fecha con las horas adecuadas para realizar la
     * consulta de movimientos, se agregan 6 horas a esta fecha dado que al momento
     * de realizar la busqueda mongodb agrega 6 horas y con estas se compenza la
     * busqueda de la misma
     */
    public static Date calculateEndDate(String year, String month, String day) {
        LocalDateTime initial = LocalDateTime.of(Integer.parseInt(year), Integer.parseInt(month), ConfrontaConstants.TEN,
                ConfrontaConstants.FIRST, ConfrontaConstants.ZERO, ConfrontaConstants.ZERO, ConfrontaConstants.ZERO);
        initial = initial.with(TemporalAdjusters.lastDayOfMonth());
        String stringDate = getFormattedDay(day, String.valueOf(initial.getDayOfMonth()))
                .concat(getFormattedMonth(month)).concat(year).concat(ConfrontaConstants.END_HOURS);
        TimeZone tz = TimeZone.getTimeZone(ConfrontaConstants.ISO_TIMEZONE);
        DateFormat df = new SimpleDateFormat(ConfrontaConstants.PATTERN_DDMMYYYY_TIME);
        df.setTimeZone(tz);
        try {
            Date formattedDate = df.parse(stringDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(formattedDate);
            calendar.add(Calendar.HOUR, ConfrontaConstants.HOURS_TO_ADD);
            return calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date orElse(Date principal, Date secondary) {
        return principal != null ? principal : secondary;
    }

    public static Date orElse(Date principal, Date secondary, Date third) {
        return principal != null ? principal : secondary != null ? secondary : third;
    }

    public static int getCurrentJulianDay() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(Objects.requireNonNull(getCurrentMexicoDate()));
        return calendar.get(GregorianCalendar.DAY_OF_YEAR);
    }

    public static String getAACCC() {
    	Integer anioRevision = Integer.parseInt(getCurrentYY())-1;
        String AA = String.valueOf(anioRevision);
        String CCC = "001";        
        return AA.concat(CCC);
    }

}
