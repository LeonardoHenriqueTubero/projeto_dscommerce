package com.leonardo.dscommerce.services;

import com.leonardo.dscommerce.dto.ProductDTO;
import com.leonardo.dscommerce.entities.Product;
import com.leonardo.dscommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository repository;

    @Autowired
    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product result = repository.findById(id).get();
        return new ProductDTO(result);
    }
}
