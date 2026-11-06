package kr.fasol.obix.core.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Util > 날짜 처리를 위한 Util
 * 프로그램명 : 날짜 처리를 위한 Util
 * 설명 : 
 *
 * @Class  DateUtil.class
 * @author  정영탁
 * @since   2017-07-26
 * @version 1.0
 *
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2017-07-26        정영탁    최초생성
 */
@Slf4j
@UtilityClass
public class DateUtil {

    /**
     * java8에서 권장하는 일자/시간 관련 함수를 사용
     */
    public static final String FORMATTER_YYYY = "yyyy";
    public static final String FORMATTER_YYYYMM = "yyyyMM";
    public static final String FORMATTER_YYYYMMDD = "yyyyMMdd";
    public static final String FORMATTER_YYYY_MM_DD = "yyyy.MM.dd";
    public static final String FORMATTER_DD = "dd";
    public static final String FORMATTER_YYYY_MM_DD_HH_MM = "yyyy.MM.dd HH:mm";
    public static final String FORMATTER_YYYY_MM_DD_HH_MM_SS = "yyyy.MM.dd HH:mm:ss";
    public static final String FORMATTER_HH_MM = "HH:mm";
    public static final String FORMATTER_HH_MM_SS = "HH:mm:ss";
    public static final String FORMATTER_HHMMSSSSS = "HHmmssSSS";
    public static final String FORMATTER_HHMMSSSS = "HHmmssSS";
    public static final String FORMATTER_HHMMSS = "HHmmss";
    public static final String FORMATTER_YYYYMMDDHHMMSSSS = "yyyyMMddHHmmssSS";
    public static final String FORMATTER_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final long MILLIS_PER_DAY = 86400000L;

    /**
     * 오늘 년월일 구하기
     */
    public static String getCurrentDate() {
        return getCurrentDate(FORMATTER_YYYYMMDD);
    }

    public static String getCurrentDate(String pattern) {
        String newPattern = StringUtils.defaultIfBlank(pattern, FORMATTER_YYYYMMDD);
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern(newPattern);
        return LocalDateTime.now().format(formatter);
    }

    /**
     * 현재시간(yyyyMMddHHmmss)
     */
    public static String getCurrentDateTime() {
        return getCurrentDate(FORMATTER_YYYYMMDDHHMMSS);
    }

    /**
     * 현재의 시분초
     */
    public static String getCurrentTime() {
        return getCurrentTime("");
    }

    /**
     * 현재의 시분초
     */
    public static String getCurrentTime(String separator) {
        Calendar cal = Calendar.getInstance();
        return String.format("%02d%s%02d%s%02d", cal.get(Calendar.HOUR_OF_DAY), separator, cal.get(Calendar.MINUTE), separator, cal.get(Calendar.SECOND));
    }

    public static String getString(Date date, String format) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 현재연도를 가져온다.
     */
    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 현재 월을 가져온다.
     */
    public static int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * 현재 일을 가져온다.
     */
    public static int getCurrentDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 현재 월의 마지막 일을 가져온다.
     */
    public static int getCurrentLastDayByMonth() {
        return Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 문자열 날짜를 DateTime 형식으로 변환
     */
    public static DateTime getDateTime(String dateStr, String format) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(format);
        return fmt.parseDateTime(dateStr);
        //DateTime dt = fmt.withZoneUTC().parseDateTime(dateStr);
        //return dt.withZoneRetainFields(DateTimeZone.getDefault());
    }

    /**
     * 날짜를 Long타입으로 리턴한다.
     */
    public static long getMillis(int year, int month, int day) {
        return (new DateTime(year, month, day, 0, 0, 0, 0)).getMillis();
    }

    /**
     * 날짜를 Long 타입으로 리턴한다.
     */
    public static long getMillis(String dateStr, String format) {
        return getDateTime(dateStr, format).getMillis();

    }

    /**
     * 날짜를 Date타입으로 리턴한다.
     */
    public static Date getDate(int year, int month, int day) {
        return (new DateTime(year, month, day, 0, 0, 0, 0)).toDate();
    }

    /**
     * 날짜를 Date 타입으로 리턴한다.
     */
    public static Date getDate(String dateStr, String format) {
        return getDateTime(dateStr, format).toDate();
    }

    /**
     * 날짜를 Calendar 타입으로 리턴한다.
     */
    public static Calendar getCalendar(int year, int month, int date) {
        return (new DateTime(year, month, date, 0, 0, 0, 0)).toCalendar(null);
    }

    /**
     * 날짜를 Calendar 타입으로 리턴한다.
     */
    public static Calendar getCalendar(String dateStr, String format) {
        return getDateTime(dateStr, format).toCalendar(null);
    }

    /**
     * 날짜를 Calendar 타입으로 리턴한다.(UTC)
     */
    public static Calendar getCalendarUTC(String dateStr, String format) {
        return getDateTime(dateStr, format).toDateTime(DateTimeZone.UTC).toCalendar(null);
    }

    /**
     * yyyyMMdd의 Calendar를 제공한다.
     */
    public static Calendar getCalendar(String yyyyMMdd) {
        int year = Integer.parseInt(yyyyMMdd.substring(0, 4));
        int month = Integer.parseInt(yyyyMMdd.substring(4, 6));
        int day = Integer.parseInt(yyyyMMdd.substring(6, 8));
        return getCalendar(year, month, day);
    }

    /**
     * 시간에 대한 셋팅
     */
    public static String getTime(String format) {
        if (StringUtils.isBlank(format)) {
            return "";
        }
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern(format.equals("A") ? "a" : format);
        return fmt.print(dt);
    }

    /**
     * 오늘날짜 리턴(UTC)
     * 예) getToday("yyyy-MM-dd a hh:mm:ss") ==> 2007-06-28 오후 05:37:46
     */
    public static String getTodayUTC(String format) {
        if (StringUtils.isBlank(format)) {
            return "";
        }
        DateTime dt = new DateTime(DateTimeZone.UTC);
        format = format.replace("Y", "y");
        format = format.replace("D", "d");
        DateTimeFormatter fmt = DateTimeFormat.forPattern(format);
        return fmt.print(dt);
    }

    /**
     * 오늘날짜 리턴 예) getToday("yyyy-MM-dd a hh:mm:ss") ==> 2007-06-28 오후 05:37:46
     */
    public static String getToday(String format) {
        if (StringUtils.isBlank(format)) {
            return "";
        }
        DateTime dt = new DateTime();
        format = format.replace("Y", "y");
        format = format.replace("D", "d");
        DateTimeFormatter fmt = DateTimeFormat.forPattern(format);
        return fmt.print(dt);
    }

    /**
     * 오늘날짜 리턴(YYYYMMDD)
     */
    public static String getToday() {
        return getToday("YYYYMMDD");
    }

    /**
     * 현재요일을 리턴한다.
     *
     * @param locale
     *            Locale.ENGLISH, Locale.KOREA
     * @param flag
     *            true = 짤은 요일명, false = 긴 요일명
     */
    public static String getWeekText(Locale locale, boolean flag) {
        DateTime dt = new DateTime();
        DateTime.Property pDoW = dt.dayOfWeek();
        return (flag ? pDoW.getAsShortText(locale) : pDoW.getAsText(locale));
    }

    /**
     * 지정된 날짜에 대한 현재요일을 리턴한다. 지정된 날짜의 형식은 반드시 "yyyyMMdd"를 지켜야 한다.
     *
     * @param date
     * @param locale
     *            Locale.ENGLISH, Locale.KOREA
     * @param flag
     *            true = 짤은 요일명, false = 긴 요일명
     */
    public static String getWeekText(String date, Locale locale, boolean flag) {
        DateTime dt = getDateTime(date, "yyyyMMdd");
        DateTime.Property pDoW = dt.dayOfWeek();
        return (flag ? pDoW.getAsShortText(locale) : pDoW.getAsText(locale));
    }

    /**
     * 현재요일을 숫자를 리턴한다.
     *
     * @param date
     * @return int [1.일 ... 7.토]
     */
    public static int getWeek(String date) {
        return getDateTime(date, "yyyyMMdd").getDayOfWeek() + 1;
    }

    /**
     * 오늘부터 year, month, day까지의 날짜 수
     */
    public static int daysBetween(int year, int month, int day) {
        DateTime dt = new DateTime();
        DateTime sdt = new DateTime(year, month, day, 0, 0, 0, 0);
        return Days.daysBetween(sdt.toLocalDateTime(), dt.toLocalDateTime()).getDays();
    }

    /**
     * 두 날짜사이의 일수차이 구함
     */
    public static int daysBetween(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
        DateTime sdt = new DateTime(startYear, startMonth, startDay, 0, 0, 0, 0);
        DateTime edt = new DateTime(endYear, endMonth, endDay, 0, 0, 0, 0);
        return Days.daysBetween(sdt.toLocalDateTime(), edt.toLocalDateTime()).getDays();
    }

    /**
     * 두 날짜사이의 일수차이 구함
     */
    public static int daysBetween(String startDate, String endDate) {
        int year = Integer.parseInt(startDate.substring(0, 4));
        int month = Integer.parseInt(startDate.substring(4, 6));
        int day = Integer.parseInt(startDate.substring(6));

        int year2 = Integer.parseInt(endDate.substring(0, 4));
        int month2 = Integer.parseInt(endDate.substring(4, 6));
        int day2 = Integer.parseInt(endDate.substring(6));

        return daysBetween(year, month, day, year2, month2, day2);
    }

    /**
     * 오늘부터 year, month, day까지의 날짜 수
     */
    public static int daysBetween(String date) {
        if (!isValidDate(date)) {
            return -1;
        }

        int[] parsedDate = parseDate(date);
        return daysBetween(parsedDate[0], parsedDate[1], parsedDate[2]);
    }

    private static boolean isValidDate(String date) {
        return date != null && date.length() == 8;
    }

    private static int[] parseDate(String date) {
        return new int[] {
                Integer.parseInt(date.substring(0, 4)),
                Integer.parseInt(date.substring(4, 6)),
                Integer.parseInt(date.substring(6))
        };
    }

    /**
     * 오늘부터 year, month, day까지의 월수 수
     */
    public static int monthsBetween(int year, int month) {
        DateTime dt = new DateTime();
        DateTime sdt = new DateTime(year, month, 1, 0, 0, 0, 0);

        return Months.monthsBetween(sdt.toLocalDateTime(), dt.toLocalDateTime()).getMonths();
    }

    /**
     * 두 날짜사이의 월수 구함
     */
    public static int monthsBetween(int startYear, int startMonth, int endYear, int endMonth) {
        DateTime sdt = new DateTime(startYear, startMonth, 1, 0, 0, 0, 0);
        DateTime edt = new DateTime(endYear, endMonth, 1, 0, 0, 0, 0);

        return Months.monthsBetween(sdt.toLocalDateTime(), edt.toLocalDateTime()).getMonths();
    }

    /**
     * 두 날짜사이의 월수 구함
     * YYYYMM, YYYYMMDD
     */
    public static int monthsBetween(String startDate, String endDate) {
        int[] start = parseAndValidateDate(startDate);
        int[] end = parseAndValidateDate(endDate);

        if (start == null || end == null) {
            return 0;
        }

        return monthsBetween(start[0], start[1], end[0], end[1]);
    }

    private static int[] parseAndValidateDate(String date) {
        if (!isValidDate(date)) {
            return null;
        }
        return splitDateInt(date);
    }

    /**
     * 오늘부터 year, month, day까지의 월수 수
     * YYYYMM, YYYYMMDD 헝
     */
    public static int monthsBetween(String date) {
        if (date == null || date.length() < 6) {
            return -1;
        }

        return monthsBetween(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)));
    }

    /**
     * 달의 날 수를 계산한다.(해당년 해당월의 말일을 구해서 리턴)
     */
    public static int getMonthDays(int year, int month) {
        DateTime dt = new DateTime(year, month, 1, 0, 0, 0, 0);
        dt = dt.plusMonths(1);
        dt = dt.minusDays(1);
        return dt.getDayOfMonth();
    }

    /**
     * 입력한 날짜와 오늘의 날짜 비교
     * @param date
     * @return 이전 날짜면 -1, 당일이면 0, 이후 날짜이면 1
     */
    public static int daysCompareTo(String date) {
        if (date == null || date.length() != 8) {
            return -1;
        }

        Calendar today = Calendar.getInstance();
    	DateTime sdt = new DateTime(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)), Integer.parseInt(date.substring(6)), 0, 0, 0, 0);
        DateTime edt = new DateTime(today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return sdt.compareTo(edt);
    }
    
    /**
     * 시작 날짜와 종 종료  날짜 비교
     * @param startDate
     * @param endDate
     * @return 이전 날짜면 -1, 당일이면 0, 이후 날짜이면 1
     */
    public static int daysCompareTo(String startDate, String endDate) {
        if ((startDate == null || startDate.length() != 8) || (endDate == null || endDate.length() != 8))
            return -1;

    	DateTime sdt = new DateTime(Integer.parseInt(startDate.substring(0, 4)), Integer.parseInt(startDate.substring(4, 6)), Integer.parseInt(startDate.substring(6)), 0, 0, 0, 0);
        DateTime edt = new DateTime(Integer.parseInt(endDate.substring(0, 4)), Integer.parseInt(endDate.substring(4, 6)), Integer.parseInt(endDate.substring(6)), 0, 0, 0, 0);

        return sdt.compareTo(edt);
    }

    /**
     * 오늘부터 year 만큼의 년을 뺀 날짜 리턴
     */
    public static String minusYears(int year) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime dt = new DateTime();
        dt = dt.minusYears(year);
        return fmt.print(dt);
    }
    
    /**
     * 오늘부터 year 만큼의 년을 뺀 날짜 리턴
     */
    public static String minusYears(int year, String pattern) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(StringUtils.defaultIfBlank(pattern, "yyyyMMdd"));
        DateTime dt = new DateTime();
        dt = dt.minusYears(year);
        return fmt.print(dt);
    }

    /**
     * 오늘부터 month 만큼의 월을 뺀 날짜 리턴
     */
    public static String minusMonths(int month) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime dt = new DateTime();
        dt = dt.minusMonths(month);
        return fmt.print(dt);
    }
    
    /**
     * 오늘부터 minusMonths 만큼의 년을 뺀 날짜 리턴
     */
    public static String minusMonths(int month, String pattern) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(StringUtils.defaultIfBlank(pattern, "yyyyMMdd"));
        DateTime dt = new DateTime();
        dt = dt.minusMonths(month);
        return fmt.print(dt);
    }

    /**
     * 오늘부터 week 만큼의 주을 뺀 날짜 리턴
     */
    public static String minusWeeks(int week) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime dt = new DateTime();
        dt = dt.minusWeeks(week);
        return fmt.print(dt);
    }

    /**
     * 오늘부터 day 만큼의 날짜를 뺀 날짜 리턴
     */
    public static String minusDays(int day) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime dt = new DateTime();
        dt = dt.minusDays(day);
        return fmt.print(dt);
    }

    /**
     * 특정날짜로부터 year 만큼의 년을 뺀 날짜 리턴
     */
    public static String minusYears(String date, int year) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime dt = getDateTime(date, "yyyyMMdd");
        dt = dt.minusYears(year);
        return fmt.print(dt);
    }

    /**
     * 특정날짜로부터 month 만큼의 월을 뺀 날짜 리턴
     */
    public static String minusMonths(String date, int month) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime dt = getDateTime(date, "yyyyMMdd");
        dt = dt.minusMonths(month);
        return fmt.print(dt);
    }

    /**
     * 특정날짜로부터 week 만큼의 주을 뺀 날짜 리턴
     */
    public static String minusWeeks(String date, int week) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime dt = getDateTime(date, "yyyyMMdd");
        dt = dt.minusWeeks(week);
        return fmt.print(dt);
    }

    /**
     * 특정날짜로부터 day 만큼의 날짜를 뺀 날짜 리턴
     */
    public static String minusDays(String date, int day) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime dt = getDateTime(date, "yyyyMMdd");
        dt = dt.minusDays(day);
        return fmt.print(dt);
    }

    /**
     * 오늘부터 year 만큼의 년을 더한 날짜 리턴
     */
    public static String plusYears(int year) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime dt = new DateTime();
        dt = dt.plusYears(year);
        return fmt.print(dt);
    }

    /**
     * 오늘부터 month 만큼의 월을 더한 날짜 리턴
     */
    public static String plusMonths(int month) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime dt = new DateTime();
        dt = dt.plusMonths(month);
        return fmt.print(dt);
    }

    /**
     * 오늘부터 week 만큼의 주을 더한 날짜 리턴
     */
    public static String plusWeeks(int week) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime dt = new DateTime();
        dt = dt.plusWeeks(week);
        return fmt.print(dt);
    }

    /**
     * 오늘부터 day 만큼의 날짜를 더한 날짜 리턴
     */
    public static String plusDays(int day) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime dt = new DateTime();
        dt = dt.plusDays(day);
        return fmt.print(dt);
    }

    /**
     * 특정날짜로부터 year 만큼의 년을 더한 날짜 리턴
     */
    public static String plusYears(String date, int year) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime dt = getDateTime(date, "yyyyMMdd");
        dt = dt.plusYears(year);
        return fmt.print(dt);
    }

    /**
     * 특정날짜로부터 month 만큼의 월을 더한 날짜 리턴
     */
    public static String plusMonths(String date, int month) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime dt = getDateTime(date, "yyyyMMdd");
        dt = dt.plusMonths(month);
        return fmt.print(dt);
    }

    /**
     * 특정날짜로부터 week 만큼의 주을 더한 날짜 리턴
     */
    public static String plusWeeks(String date, int week) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime dt = getDateTime(date, "yyyyMMdd");
        dt = dt.plusWeeks(week);
        return fmt.print(dt);
    }

    /**
     * 특정날짜로부터 day 만큼의 날짜를 더한 날짜 리턴
     */
    public static String plusDays(String date, int day) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        DateTime dt = getDateTime(date, "yyyyMMdd");
        dt = dt.plusDays(day);
        return fmt.print(dt);
    }

    /**
     * yyyyMMdd를 yyyy, mm, dd로 분리해서 String[3]로 제공한다. String[0]는 년, String[1]은 월,
     * String[2] 일.
     */
    public static String[] splitDateStr(String yyyyMMdd) {
        String[] strDate = new String[3];
        strDate[0] = yyyyMMdd.substring(0, 4);
        strDate[1] = yyyyMMdd.substring(4, 6);
        strDate[2] = yyyyMMdd.substring(6);
        return strDate;
    }

    /**
     * yyyyMMdd를 yyyy, mm, dd로 분리해서 int[3]로 제공한다. int[0]는 년, int[1]은 월, int[2]
     * 일.
     */
    public static int[] splitDateInt(String yyyyMMdd) {
        String[] strDate = splitDateStr(yyyyMMdd);
        int[] intDate = new int[3];
        intDate[0] = Integer.parseInt(strDate[0]);
        intDate[1] = Integer.parseInt(strDate[1]);
        intDate[2] = Integer.parseInt(strDate[2]);
        return intDate;
    }

    /**
     * 'yyyyMMdd' 포맷으로 문자열을 제공한다.
     */
    public static String formatYMD8(int year, int month, int day) {
        StringBuilder sb = new StringBuilder();
        sb.append(year);
        if (month < 10)
            sb.append("0");
        sb.append(month);
        if (day < 10)
            sb.append("0");
        sb.append(day);
        return sb.toString();
    }

    /**
     * yyyyMMdd의 전일을 제공한다.
     */
    public static String previousDate(String yyyyMMdd) {
        int[] intDate = splitDateInt(yyyyMMdd);
        int day = intDate[2];
        int month = intDate[1];
        int year = intDate[0];

        if (day > 1) {
            return formatYMD8(year, month, day - 1);
        } else {
            return handleEndOfMonth(year, month);
        }
    }

    private static String handleEndOfMonth(int year, int month) {
        if (month > 1) {
            month -= 1;
        } else {
            year -= 1;
            month = 12;
        }
        Calendar cal = DateUtil.getCalendar(year, month, 1);
        int lastDayOfPreviousMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return formatYMD8(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, lastDayOfPreviousMonth);
    }

    /**
     * String 과 infmt, outfmt 을 받아 날짜를 출력해준다.
     * <p>
     * ex) getFormatDate("20080901","yyyyMMdd","yyyy-MM-dd") -> 2008-09-01
     * </p>
     */
    public static String getFormatDate(String date, String infmt, String outfmt) {
        try {
            // date 형식이 infmt 에 맞지 않다면 date 를 infmt 에 맞추어줌.
            StringBuffer sDate = new StringBuffer(date);
            int len = date.length();

            if (StringUtils.equals(infmt, "yyyyMMddHHmm") && len < 12) {
                for (int i = len; i < 12; i++) {
                    sDate.append("0");
                }
            } else if (StringUtils.equals(infmt, "yyyyMMddHHmmss") && len < 14) {
                for (int i = len; i < 14; i++) {
                    sDate.append("0");
                }
            } else if (StringUtils.equals(infmt, "yyyyMMdd") && len > 8) {
            	sDate.setLength(8);
            }

            SimpleDateFormat sdf = new SimpleDateFormat(infmt, Locale.US);
            Date d = sdf.parse(sDate.toString());
            sdf.applyPattern(outfmt);

            return sdf.format(d);
        } catch (ParseException e) {
            return "";
        }
    }

    /**
     * 현재 시간을 포맷에 맞추어 내용을 수정
     * @param pattern 날자 포맷 정의, String 형식으로 출력
     * @return 포맷팅된 문자열 출력
     */
	public static String getCurrentDateTime(@NonNull String pattern) {
		String newPattern = (StringUtils.isBlank(pattern)) ? FORMATTER_YYYY_MM_DD_HH_MM_SS : pattern;
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern(newPattern);
		return LocalDateTime.now().format(formatter);
	}

    /**
     * 현재 일자 (년.월.일) 정보를 가져옴
     * @param pattern
     * @param addYear
     * @param addMonth
     * @param addDay
     * @return
     */
    public static String getCurrentDate(@NonNull String pattern, int addYear, int addMonth, int addWeek, int addDay) {
        String newPattern = StringUtils.defaultIfBlank(pattern, DateUtil.FORMATTER_YYYY_MM_DD);
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern(newPattern);
        return LocalDate.now()
                .plusYears(addYear)
                .plusMonths(addMonth)
                .plusWeeks(addWeek)
                .plusDays(addDay)
                .format(formatter);
    }

    /**
     * 만 나이 계산
     * @param resno 주민등록번호
     * @return 만 나이
     */
    public static int getAmericanAge(@NonNull String resno) {
    	if (resno.length() < 13 || !StringUtils.isNumeric(resno)) {
    		return 0;
    	}

		String birthDate = extractBirthDateFromResno(resno);

    	LocalDate now = LocalDate.now();
        return calculateAgeFromBirthdate(now, birthDate);
    }

    /**
     * 만 나이 계산
     * @param resno 주민등록번호
     * @param baseDt  기준일 - isBlank 이면 오늘
     * @return 만 나이
     */
    public static int getAmericanAge(@NonNull String resno, @NonNull String baseDt) {
        if (resno.length() < 13 || !StringUtils.isNumeric(resno)) {
            return 0;
        }

        String birthDate = extractBirthDateFromResno(resno);

        if (StringUtils.isBlank(baseDt)) {
            baseDt = DateUtil.getCurrentDate(DateUtil.FORMATTER_YYYYMMDD);
        }

        Date dt = DateUtil.getDate(baseDt, DateUtil.FORMATTER_YYYYMMDD);
        LocalDate now = dt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return calculateAgeFromBirthdate(now, birthDate);
    }

    private static String extractBirthDateFromResno(String resno) {
        String sexType = resno.substring(6, 7);
        String centuryPrefix;

        if ("1".equals(sexType) || "2".equals(sexType) || "5".equals(sexType) || "6".equals(sexType)) {
            centuryPrefix = "19";
        } else if ("9".equals(sexType) || "0".equals(sexType)) {
            centuryPrefix = "18";
        } else {
            centuryPrefix = "20";
        }

        return centuryPrefix.concat(resno.substring(0, 6));
    }

    private static int calculateAgeFromBirthdate(LocalDate now, String birthDate) {
        LocalDate parseBirthDate = LocalDate.parse(birthDate, java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));

        int age = now.minusYears(parseBirthDate.getYear()).getYear();

        // 생일이 지났는지 판단하여, 나이 보정
        if (parseBirthDate.plusYears(age).isAfter(now)) {
            age = age - 1;
        }

        return age;
    }

    /**
     * String DateTime => LocalDateTime 타입으로 변환하여 가져오기
     * @param dateTime
     * @return 예) 2022-06-10T15:53:23
     * @throws ParseException 
     */
    public static String convertLocalDateTime(@NonNull String dateTime) throws ParseException {
    	SimpleDateFormat toDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    	Date dateParse = toDateFormat.parse(dateTime);
    	
		SimpleDateFormat toSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateFormat = toSimpleDateFormat.format(dateParse);
		
		java.time.format.DateTimeFormatter toDateTimeFormat = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return java.time.LocalDateTime.parse(dateFormat, toDateTimeFormat).toString();
    }
    
    /**
     * 현재시간 LocalDateTime 타입으로 가져오기
     * @return 예) 2022-06-10T15:53:23
     */
    public static String getNowLocalDateTime() {
		LocalDateTime result = LocalDateTime.now();
		return result.toString().substring(0, 19);
	}

}
