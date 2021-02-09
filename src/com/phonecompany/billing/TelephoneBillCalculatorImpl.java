package com.phonecompany.billing;

import javax.naming.ldap.PagedResultsControl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class TelephoneBillCalculatorImpl implements TelephoneBillCalculator {

    private static final String VALUE_SEPARATOR = ",";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private static final BigDecimal RATE = BigDecimal.valueOf(0.5);
    private static final BigDecimal PRIME_TIME_RATE = BigDecimal.valueOf(1);
    private static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.2);

    @Override
    public BigDecimal calculate(String phoneLog) {
        List<PhoneLog> phoneLogList = parseLog(phoneLog);

        PhoneLog mostCommonNumber = phoneLogList
                .stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get().getKey();

        List<PhoneLog> filteredLogList = phoneLogList
                .stream()
                .filter(log -> log.equals(mostCommonNumber))
                .collect(toList());

        filteredLogList.forEach(this::countPrice);

        return filteredLogList
                .stream()
                .map(PhoneLog::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void countPrice(PhoneLog log) {
        final long minutes = getMinutes(log);

        if (isWithinPrimeTime(log)) {
            BigDecimal price = (minutes > 5) ?
                    (DISCOUNT_RATE.multiply(BigDecimal.valueOf(minutes - 5))).add(BigDecimal.valueOf(5)) :
                    PRIME_TIME_RATE.multiply(BigDecimal.valueOf(minutes));
            log.setPrice(price);
        } else {
            BigDecimal price = (minutes > 5) ?
                    (DISCOUNT_RATE.multiply(BigDecimal.valueOf(minutes - 5))).add(BigDecimal.valueOf(2.5)) :
                    RATE.multiply(BigDecimal.valueOf(minutes));
            log.setPrice(price);
        }
    }

    private boolean isWithinPrimeTime(PhoneLog phoneLog) {
        return phoneLog.getStart().getHour() > 8 && phoneLog.getEnd().getHour() < 16;
    }

    private long getMinutes(PhoneLog phoneLog) {
        long secondsDiff = ChronoUnit.SECONDS.between(phoneLog.getStart(), phoneLog.getEnd());
        return secondsDiff / 60;
    }

    private List<PhoneLog> parseLog(String log) {
        String[] lines = log.split("\\r?\\n");
        return Arrays
                .stream(lines)
                .map(line -> {
                    String[] logValues = line.split(VALUE_SEPARATOR);
                    PhoneLog phoneLog = new PhoneLog();
                    phoneLog.setNumber(logValues[0]);
                    phoneLog.setStart(LocalDateTime.parse(logValues[1], FORMATTER));
                    phoneLog.setEnd(LocalDateTime.parse(logValues[2], FORMATTER));
                    return phoneLog;
                })
                .collect(toList());
    }

}
