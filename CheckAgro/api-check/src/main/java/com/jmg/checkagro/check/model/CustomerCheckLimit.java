package com.jmg.checkagro.check.model;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "customerCheckLimit")
public class CustomerCheckLimit {

    @Id
    private String Id;
    private CustomerCheckLimit.CustomerCheckLimitId aidi;
    private BigDecimal checkAmountLimit;
    private BigDecimal checkAmountPayed;
    private BigDecimal checkAmountConsumed;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class CustomerCheckLimitId implements Serializable {
        private String documentTypeCustomer;
        private String documentValueCustomer;
    }

}
