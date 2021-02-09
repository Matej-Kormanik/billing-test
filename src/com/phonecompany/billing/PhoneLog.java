package com.phonecompany.billing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class PhoneLog {

    private String number;

    private LocalDateTime start;

    private LocalDateTime end;

    private Long lengthInMinutes;

    private BigDecimal price;

    public PhoneLog() {
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Long getLengthInMinutes() {
        return lengthInMinutes;
    }

    public void setLengthInMinutes(Long lengthInMinutes) {
        this.lengthInMinutes = lengthInMinutes;
    }

    @Override
    public String toString() {
        return "PhoneLog{" +
                "number='" + number + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneLog phoneLog = (PhoneLog) o;
        return number.equals(phoneLog.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
