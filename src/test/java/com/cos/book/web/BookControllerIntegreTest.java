package com.cos.book.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BookControllerIntegreTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private BookRepository bookRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach
	public void init() {
		entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN id RESTART WITH 1").executeUpdate();
		//entityManager.createNativeQuery("ALTER TABLE book AUTO_INCREMENT = 1").executeUpdate();
	}
	
	@Test
	public void save_테스트() throws Exception {
		
		// given
		Book book = new Book(1, "스프링 따라하기", "7.8", 300.3);
		String content = new ObjectMapper().writeValueAsString(book);
		
		// when
		ResultActions resultActions = mockMvc.perform(post("/book")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8)
					);
		
		// tehn (검증)
		resultActions
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.title").value("스프링 따라하기"))
			.andDo(MockMvcResultHandlers.print());
		
	}
	
	@Test
	public void findAll_테스트() throws Exception {
		// given
		List<Book> books = new ArrayList<>();
		books.add(new Book(8, "스프링부트 따라하기", "코스",2.0));
		books.add(new Book(9, "리엑트 따라하기", "코스",3.0));
		books.add(new Book(10, "JUnit 따라하기", "코스",4.0));
		bookRepository.saveAll(books);

		// when
		ResultActions resultAction = mockMvc.perform(get("/book").accept(MediaType.APPLICATION_JSON_UTF8));

		// then
		resultAction.andExpect(status().isOk()).andExpect(jsonPath("$.[0].id").value(1L))
				.andExpect(jsonPath("$.[2].title").value("JUnit 따라하기"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void findById_테스트() throws Exception {
		// given
		int id = 8;

		List<Book> books = new ArrayList<>();
		books.add(new Book(8, "스프링부트 따라하기", "코스",2.0));
		books.add(new Book(9, "리엑트 따라하기", "코스",3.0));
		books.add(new Book(10, "JUnit 따라하기", "코스",4.0));
		bookRepository.saveAll(books);

		// when
		ResultActions resultAction = mockMvc.perform(get("/book/{id}", id).accept(MediaType.APPLICATION_JSON_UTF8));

		// then
		resultAction.andExpect(status().isOk()).andExpect(jsonPath("$.title").value("스프링부트 따라하기"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void delete_테스트() throws Exception {
		// given
		int id = 8;

		List<Book> books = new ArrayList<>();
		books.add(new Book(8, "스프링부트 따라하기", "코스",2.0));
		books.add(new Book(9, "리엑트 따라하기", "코스",3.0));
		books.add(new Book(10, "JUnit 따라하기", "코스",4.0));
		bookRepository.saveAll(books);

		// when
		ResultActions resultAction = mockMvc.perform(delete("/book/{id}", id).accept(MediaType.TEXT_PLAIN));

		// then
		resultAction.andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());

		MvcResult requestResult = resultAction.andReturn();
		String result = requestResult.getResponse().getContentAsString();

		assertEquals("ok", result);
	}
}
