package com.Rest.RestFull.models;



import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = Task.TABLE_NAME)
public class Task {
	public static final String TABLE_NAME ="task";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name= "id", unique = true)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	
	@Column(name= "description", length = 255, nullable = false)
	@NotBlank
	@Size(min=1, max= 255)
	private String description;
	
	
	
	public Task() {
	}
	

	public Task(Long id, User user,String description) {
		super();
		this.id = id;
		this.user = user;
		this.description = description;
	}
	
	//GETTERS IN SETTERS

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null)? 0 : this.id.hashCode());
		return result;
				
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		
		if(!(obj instanceof Task)) 
			return false;
		
		Task other = (Task) obj;
		if (this.id == null)
			return false;
		else if (!this.id.equals(other.id))
			return false;
		return Objects.equals(this.id,other.id) && Objects.equals(this.user, other.user) && Objects.equals(this.description,other.description);
	}


	
	
	
	
	
	
	
	

}
