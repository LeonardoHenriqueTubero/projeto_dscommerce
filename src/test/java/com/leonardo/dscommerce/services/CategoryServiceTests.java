package com.leonardo.dscommerce.services;

import com.leonardo.dscommerce.dto.CategoryDTO;
import com.leonardo.dscommerce.entities.Category;
import com.leonardo.dscommerce.repositories.CategoryRepository;
import com.leonardo.dscommerce.tests.CategoryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

    @InjectMocks
    private CategoryService service;

    @Mock
    private CategoryRepository repository;

    private Category category;
    private List<Category> list;

    @BeforeEach
    void setUp() throws Exception{
        category = CategoryFactory.createCategory();
        list = new ArrayList<>();
        list.add(category);

        Mockito.when(repository.findAll()).thenReturn(list);
    }

    @Test
    public void findAllShouldReturnListCategoryDTO() {
        List<CategoryDTO> result = service.findAll();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(result.getFirst().getId(), category.getId());
        Assertions.assertEquals(result.getFirst().getName(), category.getName());

    }
}
