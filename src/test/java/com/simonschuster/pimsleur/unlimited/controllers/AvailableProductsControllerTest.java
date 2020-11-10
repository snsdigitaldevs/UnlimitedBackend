package com.simonschuster.pimsleur.unlimited.controllers;

import com.simonschuster.pimsleur.unlimited.data.dto.availableProducts.AvailableProductsDto;
import com.simonschuster.pimsleur.unlimited.data.dto.freeLessons.AvailableProductDto;
import com.simonschuster.pimsleur.unlimited.services.availableProducts.AvailableProductsService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AvailableProductsControllerTest extends TestCase {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private AvailableProductsService availableProductsService;

	@Test
	public void should_get_orderly_available_products_when_call_available_products_api_given_a_valid_sub_email_store() throws Exception {
		String sub = "auth0|5f5f059c82c46c006cbc89f8";
		String email = "hebrew_prem_2@ss.com";
		String storeDomain = "webapp";
		List<AvailableProductDto> purchasedProducts = new ArrayList<>();
		AvailableProductDto availableProductDto1 = new AvailableProductDto("English for Arabic Speakers Level 1 Lessons 11-15", "Arabic", "978179788812111", false);
		AvailableProductDto availableProductDto2 = new AvailableProductDto("English for Arabic Speakers Level 1 Lessons 6-10", "Arabic", "978179788812511", false);
		AvailableProductDto availableProductDto3 = new AvailableProductDto("Dari Persian Subscription", "Dari", "978179788812911", false);
		purchasedProducts.add(availableProductDto1);
		purchasedProducts.add(availableProductDto2);
		purchasedProducts.add(availableProductDto3);
		List<AvailableProductDto> freeProducts = Collections.emptyList();
		AvailableProductsDto availableProductsDto = new AvailableProductsDto(purchasedProducts, freeProducts);
		when(availableProductsService.getAvailableProducts(sub, email, storeDomain)).thenReturn(availableProductsDto);

		mockMvc.perform(get("/availableProducts").param("sub", sub).param("email", email).param("storeDomain", storeDomain))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.purchasedProducts[1].productCode").value("978179788812511"))
			.andExpect(jsonPath("$.purchasedProducts[1].courseName").value("English for Arabic Speakers Level 1 Lessons 6-10"));
	}
}