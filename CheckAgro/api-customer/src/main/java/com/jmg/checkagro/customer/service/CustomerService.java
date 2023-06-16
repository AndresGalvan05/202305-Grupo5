package com.jmg.checkagro.customer.service;

import com.jmg.checkagro.customer.client.CheckMSClient;
import com.jmg.checkagro.customer.event.RegisterCustomerProducer;
import com.jmg.checkagro.customer.exception.CustomerException;
import com.jmg.checkagro.customer.exception.MessageCode;
import com.jmg.checkagro.customer.model.Customer;
import com.jmg.checkagro.customer.repository.CustomerRepository;
import com.jmg.checkagro.customer.utils.DateTimeUtils;
import com.netflix.discovery.converters.Auto;
import feign.Feign;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CustomerService {


    private final CustomerRepository customerRepository;
    private final RegisterCustomerProducer registerCustomerProducer;

//    @Value("${urlCheck}")
//    private String urlCheck;


    private CheckMSClient checkMSClient;
    @Autowired
    public CustomerService(CustomerRepository customerRepository,CheckMSClient checkMSClient, RegisterCustomerProducer registerCustomerProducer) {
        this.customerRepository = customerRepository;
        this.checkMSClient = checkMSClient;
        this.registerCustomerProducer = registerCustomerProducer;
    }

    @Transactional
    public Long create(Customer entity) throws CustomerException {
        if(customerRepository.findByDocumentTypeAndDocumentNumber(entity.getDocumentType(),entity.getDocumentNumber()).isPresent()){
            throw new CustomerException(MessageCode.CUSTOMER_EXISTS);
        }
        entity.setCreation(DateTimeUtils.now());
        entity.setActive(true);
        customerRepository.save(entity);
        registerCustomerProducer.publishRegisterCustomer(new RegisterCustomerProducer.Data(entity.getDocumentType(), entity.getDocumentNumber()));
        CheckMSClient.DocumentRequest entidad = new CheckMSClient.DocumentRequest(entity.getDocumentType(), entity.getDocumentNumber());
        createCustomer(entidad);
        return entity.getId();
    }

    @Retry(name = "retryCustomer")
    @CircuitBreaker(name = "clientCustomer", fallbackMethod = "createCustomerFallBack")
    private void createCustomer(CheckMSClient.DocumentRequest entity) {
        checkMSClient.registerCustomer(entity);
    }

    public void createCustomerFallBack(CheckMSClient.DocumentRequest documentRequest, Throwable t) throws Exception {
        throw new Exception("Could not register customer");
    }

//    private void registerCustomerInMSCheck(Customer entity) {
//        CheckMSClient client = Feign.builder()
////                .encoder(new JacksonEncoder())
//                .target(CheckMSClient.class, urlCheck);
//        client.registerCustomer(CheckMSClient.DocumentRequest.builder()
//                .documentType(entity.getDocumentType())
//                .documentValue(entity.getDocumentNumber())
//                .build());
//    }

//    private void deleteCustomerInMSCheck(Customer entity) {
//        CheckMSClient client = Feign.builder()
//                .encoder(new JacksonEncoder())
//                .target(CheckMSClient.class, urlCheck);
//        client.deleteCustomer(CheckMSClient.DocumentRequest.builder()
//                .documentType(entity.getDocumentType())
//                .documentValue(entity.getDocumentNumber())
//                .build());
//    }

    public void update(Long id, Customer entity) throws CustomerException {
        var entityUpdate = customerRepository.findById(id).orElseThrow(() -> new CustomerException(MessageCode.CUSTOMER_NOT_FOUND));
        entity.setDocumentType(entityUpdate.getDocumentType());
        entity.setDocumentNumber(entityUpdate.getDocumentNumber());
        entity.setId(entityUpdate.getId());
        entity.setActive(entityUpdate.getActive());
        entity.setCreation(entityUpdate.getCreation());
        customerRepository.save(entity);
    }

    public void deleteById(Long id) throws CustomerException {
        var entityDelete = customerRepository.findById(id).orElseThrow(() -> new CustomerException(MessageCode.CUSTOMER_NOT_FOUND));
        customerRepository.updateActive(false, id);

        deleteCustomer(new CheckMSClient.DocumentRequest(entityDelete.getDocumentType(), entityDelete.getDocumentNumber()));
    }

    @Retry(name = "retryCustomer")
    @CircuitBreaker(name = "clientCustomer", fallbackMethod = "deleteCustomerFallBack")
    private void deleteCustomer(CheckMSClient.DocumentRequest entity) {
        checkMSClient.deleteCustomer(entity);
    }

    public void deleteCustomerFallBack(CheckMSClient.DocumentRequest documentRequest, Throwable t) throws Exception {
        throw new Exception("Could not delete customer");
    }

    public Customer getById(Long id) throws CustomerException {
        return customerRepository.findByIdAndActive(id, true).orElseThrow(() -> new CustomerException(MessageCode.CUSTOMER_NOT_FOUND));
    }
}
