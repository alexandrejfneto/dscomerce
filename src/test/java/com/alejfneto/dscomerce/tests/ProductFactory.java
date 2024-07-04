package com.alejfneto.dscomerce.tests;

import com.alejfneto.dscomerce.entities.Category;
import com.alejfneto.dscomerce.entities.Product;

public class ProductFactory {
	
	public static Product createProduct() {
		Category category = CategoryFactory.createCategory();
		Product product = new Product(1L, "Playstation", "Uma descricao qualquer", 2000.0, "url.com.br");
		product.getCategories().add(category);
		return product;
	}
	
	public static Product createProduct(String name) {
		Product product = createProduct();
		product.setName(name);
		return product;
	}

}
