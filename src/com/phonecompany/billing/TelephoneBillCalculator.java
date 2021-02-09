package com.phonecompany.billing;

import java.math.BigDecimal;

@FunctionalInterface
public interface TelephoneBillCalculator {

    BigDecimal calculate (String phoneLog);

}

