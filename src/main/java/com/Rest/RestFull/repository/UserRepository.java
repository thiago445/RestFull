package com.Rest.RestFull.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Rest.RestFull.models.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
	


}
