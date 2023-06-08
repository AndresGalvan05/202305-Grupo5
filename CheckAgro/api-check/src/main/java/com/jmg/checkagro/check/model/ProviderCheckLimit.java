package com.jmg.checkagro.check.model;


import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "providerCheckLimit")
public class ProviderCheckLimit {
    @Id
    private String Id;
    private ProviderCheckLimit.ProviderCheckLimitId aidi;
    private Boolean active;
    private BigDecimal checkAmountReceived;
    private BigDecimal checkAmountActive;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class ProviderCheckLimitId implements Serializable {
        private String documentTypeProvider;
        private String documentValueProvider;
    }
}
