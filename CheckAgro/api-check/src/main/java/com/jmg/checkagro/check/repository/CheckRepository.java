package com.jmg.checkagro.check.repository;

import com.jmg.checkagro.check.model.Check;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CheckRepository extends MongoRepository<Check,String> {
    List<Check> findByDocumentTypeCustomerAndDocumentValueCustomer(String typeDocument, String valueDocument);

    List<Check> findByDocumentTypeProviderAndDocumentValueProvider(String typeDocument, String valueDocument);
}
