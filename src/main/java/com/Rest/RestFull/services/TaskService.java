package com.Rest.RestFull.services;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Rest.RestFull.models.ProfileEnum;
import com.Rest.RestFull.models.Task;
import com.Rest.RestFull.models.User;
import com.Rest.RestFull.repository.TaskRepository;
import com.Rest.RestFull.security.UserSpringSecurity;
import com.Rest.RestFull.services.Exceptions.AuthorizationExeption;
import com.Rest.RestFull.services.Exceptions.DataBindingViolationException;
import com.Rest.RestFull.services.Exceptions.ObjectNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private UserService userService;

	public List<Task> findAllByUser() {
		UserSpringSecurity userSpringSecurity = UserService.authenticated();

		if (Objects.isNull(userSpringSecurity)) {
			throw new AuthorizationExeption("Acesso negado");
		}

		List<Task> tasks = this.taskRepository.findByUser_Id(userSpringSecurity.getId());
		return tasks;
	}

	public Task findById(Long id) {

		Task task = this.taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
				"Tarefa nao encontrada! id " + id + ", tipo: " + Task.class.getName()));

		UserSpringSecurity userSpringSecurity = UserService.authenticated();

		if (Objects.isNull(userSpringSecurity)
				|| !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !userHastask(userSpringSecurity, task)) {
			throw new AuthorizationExeption("Acesso negado");
		}
		return task;
	}

	public List<Task> findAll() {
		List<Task> task = this.taskRepository.findAll();

		UserSpringSecurity userSpringSecurity = UserService.authenticated();

		if (userSpringSecurity.hasRole(ProfileEnum.ADMIN)) {
			return task;
		}
		throw new AuthorizationExeption("Não autorizado!");
	}

	@Transactional
	public Task create(Task obj) {
		UserSpringSecurity userSpringSecurity = UserService.authenticated();
		if (Objects.isNull(userSpringSecurity)) {
			throw new AuthorizationExeption("Acesso negado");
		}
		User user = this.userService.findById(userSpringSecurity.getId());

		obj.setId(null);
		obj.setUser(user);
		obj = this.taskRepository.save(obj);
		return obj;
	}

	@Transactional
	public Task update(Task obj) {
		Task newObj = findById(obj.getId());
		newObj.setDescription(obj.getDescription());
		return this.taskRepository.save(newObj);
	}

	public void delete(Long id) {
		try {
			this.taskRepository.deleteById(id);
		} catch (Exception e) {
			throw new DataBindingViolationException("Não é possivel excluir pois há entidade relacionadas!");
		}
	}

	private Boolean userHastask(UserSpringSecurity userSpringSecurity, Task task) {
		return task.getUser().getId().equals(userSpringSecurity.getId());

	}

}
