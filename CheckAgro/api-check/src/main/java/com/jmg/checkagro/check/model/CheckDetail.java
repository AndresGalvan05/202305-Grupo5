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
public class CheckDetail implements Serializable {
    @Id
    private String id;
    private String concept;
    private BigDecimal amountUnit;
    private Integer quantity;
    private Check checkVirtual;
}
