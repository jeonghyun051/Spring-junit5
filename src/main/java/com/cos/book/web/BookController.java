package com.cos.book.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BookController {
	
	private final BookRepository bookRepository;
	
	@CrossOrigin
	@PostMapping("/book")
	public ResponseEntity<?> save(@RequestBody Book book){
		return new ResponseEntity<>(bookRepository.save(book), HttpStatus.CREATED);
	}

	@CrossOrigin
	@GetMapping("/book")
	public ResponseEntity<?> findAll(){
		System.out.println("findAll 실행됨");
		return new ResponseEntity<>(bookRepository.findAll(), HttpStatus.OK);
	}
	
	@CrossOrigin
	@GetMapping("/book/{id}")
	public ResponseEntity<?> findById(@PathVariable int id){
		System.out.println("findById 실행됨");
		return new ResponseEntity<>(bookRepository.findById(id),HttpStatus.OK);
		
	}
	
	@CrossOrigin
	@PutMapping("/book/{id}")
	public ResponseEntity<?> findById(@PathVariable int id, @RequestBody Book book){
		Book bookEntity = bookRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("id를 확인해주세요!!"));
		bookEntity.setTitle(book.getTitle());
		bookEntity.setPrice(book.getPrice());
		bookEntity.setRating(book.getRating());
				
		return new ResponseEntity<>(bookRepository.findById(id),HttpStatus.OK);
		
	}
	
	@DeleteMapping("/book/{id}")
	public ResponseEntity<?> deleteById(@PathVariable int id){
		bookRepository.deleteById(id);
		return new ResponseEntity<>("ok",HttpStatus.OK);
		
	}
}
