package com.jmg.checkagro.check.model;


import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Document(collection = "providerCheckLimit")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProviderCheckLimit {
    @EmbeddedId
    private ProviderCheckLimit.ProviderCheckLimitId id;
    private Boolean active;
    private BigDecimal checkAmountReceived;
    private BigDecimal checkAmountActive;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    @Embeddable
    public static class ProviderCheckLimitId implements Serializable {
        private String documentTypeProvider;
        private String documentValueProvider;
    }
}
