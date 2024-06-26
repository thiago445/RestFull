package com.Rest.RestFull.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Rest.RestFull.models.Task;
import com.Rest.RestFull.models.projection.TaskProjection;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

	//SEM CODIGO SQL
	
	List<TaskProjection> findByUser_Id(Long id);
	
	//MEIO SQL
	
	//@Query(value ="SELECT t FROM Task t WERE t user.id")
	//List<Task> findByUser_Id(@Param ("id" )Long id);
	
	//TOTALMENTE SQL
	
	//@Query(value ="SELECT * FROM task t WHERE t.user_id = :id",nativeQuery = true)
	//List<Task> findByUser_Id(@Param ("id" )Long id);
	
	
	
}
