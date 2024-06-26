package com.Rest.RestFull.services;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Rest.RestFull.models.ProfileEnum;
import com.Rest.RestFull.models.User;
import com.Rest.RestFull.models.UserCreateDTO;
import com.Rest.RestFull.models.UserUpdateDTO;
import com.Rest.RestFull.repository.UserRepository;
import com.Rest.RestFull.security.UserSpringSecurity;
import com.Rest.RestFull.services.Exceptions.AuthorizationExeption;
import com.Rest.RestFull.services.Exceptions.DataBindingViolationException;
import com.Rest.RestFull.services.Exceptions.ObjectNotFoundException;

import jakarta.validation.Valid;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	public User findById(Long id) {
		UserSpringSecurity userSpringSecurity = authenticated();
		if (!Objects.nonNull(userSpringSecurity)
				|| !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !id.equals(userSpringSecurity.getId())) {
			throw new AuthorizationExeption("Acesso negado!");
		}
		Optional<User> user = this.userRepository.findById(id);
		return user.orElseThrow(() -> new ObjectNotFoundException(
				"Usuario não encontrado! id: " + id + ",tipo: " + User.class.getName()));
	}
	
	

	@Transactional
	public User create(User obj) {
		obj.setId(null);
		obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
		obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
		obj = this.userRepository.save(obj);
		return obj;

	}
	

	@Transactional
	public User update(User obj) {
		User newObj = findById(obj.getId());
		newObj.setPassword(obj.getPassword());
		newObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
		return this.userRepository.save(newObj);
	}

	public void delete(Long id) {
		findById(id);
		try {
			this.userRepository.deleteById(id);

		} catch (Exception e) {
			throw new DataBindingViolationException("Não é possivel excluir pois há entidade relacionadas!");
		}
	}
	
	public User fromDTO(@Valid UserCreateDTO obj) {
		User user = new User();
		user.setUsername(obj.getUsername());
		user.setPassword(obj.getPassword());
		return user;
	}
	public User fromDTO(@Valid UserUpdateDTO obj) {
		User user = new User();
		user.setId(obj.getId());
		user.setPassword(obj.getPassword());
		return user;
	}

	public static UserSpringSecurity authenticated() {
		try {
			return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}

}
