package com.jmg.checkagro.check.repository;

import com.jmg.checkagro.check.model.ProviderCheckLimit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderCheckLimitRepository extends MongoRepository<ProviderCheckLimit, ProviderCheckLimit.ProviderCheckLimitId> {
}
