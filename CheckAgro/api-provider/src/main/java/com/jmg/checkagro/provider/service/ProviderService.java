package com.jmg.checkagro.provider.service;

import com.jmg.checkagro.provider.client.CheckMSClient;
import com.jmg.checkagro.provider.exception.MessageCode;
import com.jmg.checkagro.provider.exception.ProviderException;
import com.jmg.checkagro.provider.model.Provider;
import com.jmg.checkagro.provider.repository.ProviderRepository;
import com.jmg.checkagro.provider.utils.DateTimeUtils;
import com.netflix.discovery.converters.Auto;
import feign.Feign;
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

        checkMSClient.registerProvider(new CheckMSClient.DocumentRequest(entity.getDocumentType(), entity.getDocumentNumber()));
        return entity.getId();
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
        checkMSClient.deleteProvider(new CheckMSClient.DocumentRequest(entityDelete.getDocumentType(), entityDelete.getDocumentNumber()));
    }

    public Provider getById(Long id) throws ProviderException {
        return providerRepository.findByIdAndActive(id, true).orElseThrow(() -> new ProviderException(MessageCode.PROVIDER_NOT_FOUND));
    }
}
