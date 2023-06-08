package com.jmg.checkagro.check.model;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "Check")
public class Check implements Serializable {


    @Id
    private String Id;
    private String documentTypeCustomer;
    private String documentValueCustomer;
    private String documentTypeProvider;
    private String documentValueProvider;
    private LocalDateTime emitDate;
    private BigDecimal amountTotal;
    private Integer monthsDuration;
    private BigDecimal commissionAgro;
    private String stateCheck;
    private Set<CheckDetail> checkDetails;
}
