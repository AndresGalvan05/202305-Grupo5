package com.jmg.checkagro.check.model;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Document(collection = "customerCheckLimit")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomerCheckLimit {

    @EmbeddedId
    private CustomerCheckLimit.CustomerCheckLimitId id;
    private BigDecimal checkAmountLimit;
    private BigDecimal checkAmountPayed;
    private BigDecimal checkAmountConsumed;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    @Embeddable
    public static class CustomerCheckLimitId implements Serializable {
        private String documentTypeCustomer;
        private String documentValueCustomer;
    }

}
