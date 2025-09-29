package com.leonardo.dscommerce.services;

import com.leonardo.dscommerce.dto.ProductDTO;
import com.leonardo.dscommerce.entities.Product;
import com.leonardo.dscommerce.repositories.ProductRepository;
import com.leonardo.dscommerce.services.exceptions.ResourceNotFoundException;
import com.leonardo.dscommerce.tests.ProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    private Long existingId, nonExistingId;
    private Product product;
    private String productName;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 2L;
        productName = "Playstation 5";

        product = ProductFactory.createProduct(productName);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
    }

    @Test
    void findByIdShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);
        Assertions.assertEquals(result.getName(), product.getName());
    }
}
