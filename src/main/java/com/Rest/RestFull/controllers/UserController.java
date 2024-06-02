package com.Rest.RestFull.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.Rest.RestFull.models.User;
import com.Rest.RestFull.models.UserCreateDTO;
import com.Rest.RestFull.models.UserUpdateDTO;
import com.Rest.RestFull.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
	//PATHVARIABLE É ULTILIZADO PARA O GET MAPPING ENTENDER O QUE É ESSA VAREAVEL ENTRE CHAVES
	@GetMapping("/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		
		User obj = this.userService.findById(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@PostMapping
	public ResponseEntity<Void> create(@Valid @RequestBody UserCreateDTO obj){
		User user = this.userService.fromDTO(obj);
		User newUser = this.userService.create(user);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/[id]").buildAndExpand(newUser.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody UserUpdateDTO obj, @PathVariable Long id){
		obj.setId(id);
		User user = this.userService.fromDTO(obj);
		
		this.userService.update(user);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		this.userService.delete(id);
		return ResponseEntity.noContent().build();
	}
	

}
