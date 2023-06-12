package com.jmg.checkagro.provider.service;

import com.jmg.checkagro.provider.client.CheckMSClient;
import com.jmg.checkagro.provider.exception.MessageCode;
import com.jmg.checkagro.provider.exception.ProviderException;
import com.jmg.checkagro.provider.model.Provider;
import com.jmg.checkagro.provider.repository.ProviderRepository;
import com.jmg.checkagro.provider.utils.DateTimeUtils;
import com.netflix.discovery.converters.Auto;
import feign.Feign;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ProviderService {

    private final ProviderRepository providerRepository;

    private CheckMSClient checkMSClient;
    @Autowired
    public ProviderService(ProviderRepository providerRepository, CheckMSClient checkMSClient) {
        this.providerRepository = providerRepository;
        this.checkMSClient = checkMSClient;
    }

    @Transactional
    public Long create(Provider entity) throws ProviderException {
        if(providerRepository.findByDocumentTypeAndDocumentNumber(entity.getDocumentType(),entity.getDocumentNumber()).isPresent()){
            throw new ProviderException(MessageCode.PROVIDER_EXISTS);
        }
        entity.setCreation(DateTimeUtils.now());
        entity.setActive(true);
        providerRepository.save(entity);

        registerProvider(new CheckMSClient.DocumentRequest(entity.getDocumentType(), entity.getDocumentNumber()));
        return entity.getId();
    }

    @Retry(name = "retryProvider")
    @CircuitBreaker(name = "clientProvider", fallbackMethod = "registerProviderFallBack")
    private void registerProvider(CheckMSClient.DocumentRequest entity) {
        checkMSClient.registerProvider(entity);
    }

    public void registerProviderFallBack(CheckMSClient.DocumentRequest entity, Throwable t) throws Exception {
        throw new Exception("Could not register provider");
    }

    public void update(Long id, Provider entity) throws ProviderException {
        var entityUpdate = providerRepository.findById(id).orElseThrow(() -> new ProviderException(MessageCode.PROVIDER_NOT_FOUND));
        entity.setDocumentType(entityUpdate.getDocumentType());
        entity.setDocumentNumber(entityUpdate.getDocumentNumber());
        entity.setId(entityUpdate.getId());
        entity.setActive(entityUpdate.getActive());
        entity.setCreation(entityUpdate.getCreation());
        providerRepository.save(entity);
    }

    public void deleteById(Long id) throws ProviderException {
        var entityDelete = providerRepository.findById(id).orElseThrow(() -> new ProviderException(MessageCode.PROVIDER_NOT_FOUND));
        providerRepository.updateActive(false, id);
        deleteProvider(new CheckMSClient.DocumentRequest(entityDelete.getDocumentType(), entityDelete.getDocumentNumber()));
    }

    @Retry(name = "retryProvider")
    @CircuitBreaker(name = "clientProvider", fallbackMethod = "deleteProviderFallBack")
    private void deleteProvider(CheckMSClient.DocumentRequest entityDelete) {
        checkMSClient.deleteProvider(entityDelete);
    }

    public void deleteProviderFallBack(CheckMSClient.DocumentRequest documentRequest, Throwable t) throws Exception {
        throw new Exception("Could not delete provider");
    }

    public Provider getById(Long id) throws ProviderException {
        return providerRepository.findByIdAndActive(id, true).orElseThrow(() -> new ProviderException(MessageCode.PROVIDER_NOT_FOUND));
    }
}
