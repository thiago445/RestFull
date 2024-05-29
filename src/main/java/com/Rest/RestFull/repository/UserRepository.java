package com.Rest.RestFull.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.Rest.RestFull.models.User;



@Repository
public interface UserRepository extends JpaRepository<User,Long>{
	
	@Transactional(readOnly = true)
	User findByname(String username);
	


}
