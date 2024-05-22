package com.Rest.RestFull.
services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Rest.RestFull.models.Task;
import com.Rest.RestFull.models.User;
import com.Rest.RestFull.repository.TaskRepository;

import jakarta.transaction.Transactional;

@Service
public class TaskService {
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserService userService;
	
	
	public List<Task>findAllById(Long userId){
		List<Task> tasks = this.taskRepository.findByUser_Id(userId);
		return tasks;
	}
	
	public Task findById(Long id) {
		Optional<Task>task = this.taskRepository.findById(id);
		return task.orElseThrow(() -> new RuntimeException(
				"Tarefa nao encontrada! id "+id + ", tipo: "+ Task.class.getName()));
		
	}
	
	@Transactional
	public Task create(Task obj) {
		User user = this.userService.findById(obj.getUser().getId());
		obj.setId(null);
		obj.setUser(user);
		obj= this.taskRepository.save(obj);
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
			throw new RuntimeException("Não é possivel excluir pois há entidade relacionadas!");
		}
	}
	
}
