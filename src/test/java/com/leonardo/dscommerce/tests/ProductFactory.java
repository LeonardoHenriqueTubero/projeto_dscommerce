package com.leonardo.dscommerce.tests;

import com.leonardo.dscommerce.entities.Category;
import com.leonardo.dscommerce.entities.Product;

public class ProductFactory {

    public static Product createProduct() {
        Category category = CategoryFactory.createCategory();
        Product product = new Product(1L, "Console Playstation 5", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam non pulvinar nisl. Curabitur ornare ultricies sapien nec dignissim. Nulla sagittis, augue sit amet auctor dapibus, purus eros vestibulum neque, feugiat placerat neque leo a neque. In ut vestibulum massa, a convallis tortor. Ut diam tellus, suscipit non tincidunt in, facilisis sed libero.",
                3.99, "img.jpg");
        product.getCategories().add(category);
        return product;
    }

    public static Product createProduct(String name) {
        Product product = createProduct();
        product.setName(name);
        return product;
    }
}
