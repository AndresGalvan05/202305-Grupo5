package com.jmg.checkagro.check.repository;

import com.jmg.checkagro.check.model.CustomerCheckLimit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerCheckLimitRepository extends MongoRepository<CustomerCheckLimit, CustomerCheckLimit.CustomerCheckLimitId > {
}
