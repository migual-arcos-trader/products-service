//package com.marcos.products_service.integration;
//
//import com.marcos.products_service.dto.ProductDTO;
//import com.marcos.products_service.model.Product;
//import com.marcos.products_service.repository.ProductRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.test.context.ActiveProfiles;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test") // Activa application-test.properties
//class ProductIntegrationTest {
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Test
//    void shouldPersistProductInDatabase() {
//        // Arrange
//        ProductDTO dto = new ProductDTO("Mouse", 100.0, "Inalámbrico");
//
//        // Act
//        var response = restTemplate.postForEntity(
//                "http://localhost:" + port + "/products",
//                dto,
//                String.class
//        );
//
//        // Assert
//        assertThat(response.getStatusCode().value()).isEqualTo(201);
//
//        var all = productRepository.findAll();
//        assertThat(all).hasSize(1);
//
//        Product saved = all.get(0);
//        assertThat(saved.getName()).isEqualTo("Mouse");
//        assertThat(saved.getPrice()).isEqualTo(100.0);
//    }
//}
