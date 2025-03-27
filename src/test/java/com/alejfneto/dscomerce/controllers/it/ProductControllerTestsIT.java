package com.alejfneto.dscomerce.controllers.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alejfneto.dscomerce.dto.ProductDTO;
import com.alejfneto.dscomerce.entities.Product;
import com.alejfneto.dscomerce.tests.ProductFactory;
import com.alejfneto.dscomerce.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerTestsIT {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private ObjectMapper objectMapper;

	private String productName;
	
	private ProductDTO productDTO;
	private Product product;
	
	private String adminUsername, clientUsername, adminPassword, clientPassword;
	private String adminToken, clientToken, invalidToken;
	
	private Long productIdExisting, productIdDependent, productIdNonExisting;

	@BeforeEach
	void setUp() throws Exception {
		
		adminUsername = "alex@gmail.com";
		adminPassword = "123456";
		clientUsername = "maria@gmail.com";
		clientPassword = "123456";

		productName = "MacBook";
		
		adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
		invalidToken = adminToken + "xpto"; //Simulates wrong password
		
		product = ProductFactory.createProduct();
		product.setId(null);
		productDTO = new ProductDTO(product);
		
		productIdExisting = 12L;
		productIdDependent = 1L;
		productIdNonExisting = 2000L;

	}

	@Test
	public void findAllShouldReturnPageWhenNameParamIsNotEmpty() throws Exception {

		ResultActions result = mockMvc
				.perform(get("/products?name={productName}", productName)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content[0].id").value(3L));
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[0].price").value(1250.0));
		result.andExpect(jsonPath("$.content[0].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg"));
		
	}
	
	@Test
	public void findAllShouldReturnPageWhenNameParamIsEmpty() throws Exception {
		
		ResultActions result = mockMvc
				.perform(get("/products")
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content[0].id").value(1L));
		result.andExpect(jsonPath("$.content[0].name").value("The Lord of the Rings"));
		result.andExpect(jsonPath("$.content[0].price").value(90.5));
		result.andExpect(jsonPath("$.content[0].imgUrl").value("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg"));
		
	}
	
	@Test
	public void insertShouldReturnProductDTOCreatedWhenAdminloggedAndDataProductValid() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").value(26L));
		result.andExpect(jsonPath("$.name").value("Playstation"));
		result.andExpect(jsonPath("$.description").value("Uma descricao qualquer"));
		result.andExpect(jsonPath("$.price").value(2000.0));
		result.andExpect(jsonPath("$.imgUrl").value("url.com.br"));
		
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminloggedAndProductInvalidName() throws Exception {
		
		product.setName("");
		productDTO = new ProductDTO(product);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + adminToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isUnprocessableEntity());
		
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientlogged() throws Exception {
	
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + clientToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isForbidden());
		
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
	
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc
				.perform(post("/products")
						.header("Authorization", "Bearer " + invalidToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isUnauthorized());
		
	}
	
	@Test
	public void deleteShouldReturnNoContentWhenAdminloggedAndProductIdExisting() throws Exception {
		
		ResultActions result = mockMvc
				.perform(delete("/products/{id}", productIdExisting)
						.header("Authorization", "Bearer " + adminToken)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isNoContent());
		
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenAdminLoggedAndProductInexistId() throws Exception {
		
		ResultActions result = mockMvc
				.perform(delete("/products/{id}", productIdNonExisting)
						.header("Authorization", "Bearer " + adminToken)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isNotFound());
		
	}
	
	@Test
	@Transactional (propagation = Propagation.SUPPORTS)
	public void deleteShouldReturnBadRequestWhenAdminLoggedAndProductDependentId() throws Exception {

		ResultActions result = mockMvc
				.perform(delete("/products/{id}", productIdDependent)
						.header("Authorization", "Bearer " + adminToken)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isBadRequest());
		
	}
	
	@Test
	public void deleteShouldReturnForbiddenWhenClientLogged() throws Exception {

		ResultActions result = mockMvc
				.perform(delete("/products/{id}", productIdExisting)
						.header("Authorization", "Bearer " + clientToken)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isForbidden());
		
	}
	
	@Test
	public void deleteShouldReturnUnauthorizedWhenInvalidToken() throws Exception {

		ResultActions result = mockMvc
				.perform(delete("/products/{id}", productIdExisting)
						.header("Authorization", "Bearer " + invalidToken)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isUnauthorized());
		
	}


}
