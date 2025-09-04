package com.leonardo.dscommerce.controllers;

import com.leonardo.dscommerce.dto.CategoryDTO;
import com.leonardo.dscommerce.dto.ProductDTO;
import com.leonardo.dscommerce.dto.ProductMinDTO;
import com.leonardo.dscommerce.services.CategoryService;
import com.leonardo.dscommerce.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    private final CategoryService service;

    @Autowired
    public CategoryController(CategoryService service) {
        this.service = service;
    }


    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll() {
        List<CategoryDTO> dtos = service.findAll();
        return ResponseEntity.ok(dtos);
    }
}