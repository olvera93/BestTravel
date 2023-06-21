package com.olvera.best_travel.infraestructure.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Map;

@Data
public class CurrencyDto implements Serializable {

    @JsonProperty(value = "date")
    private LocalDate exchangeDate;

    private Map<Currency, BigDecimal> rates;


}
