package com.Rest.RestFull.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = User.TABLE_NAME)
@Getter
@Setter
public class User {
	
	public interface CreateUser{}
	public interface UpdateUser{}

	public static final String TABLE_NAME= "user";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique=true)
	private Long id;
	
	@NotNull(groups = CreateUser.class)
	@NotEmpty(groups = CreateUser.class)
	@Size(groups = CreateUser.class , min=2, max=100)
	@Column(name= "username", length = 100, nullable = false, unique = true)
	private String username;
	
	@JsonProperty(access= Access.WRITE_ONLY)
	@Column(name = "password", length = 60, nullable = false)
	@NotNull(groups = {CreateUser.class, UpdateUser.class})
	@NotEmpty(groups = {CreateUser.class, UpdateUser.class})
	@Size(groups = {CreateUser.class, UpdateUser.class} ,min = 8, max =60)
	private String password;
	
	@OneToMany(mappedBy= "user")
	private List<Task> tasks= new ArrayList<Task>();
	
	
	@ElementCollection(fetch = FetchType.EAGER)//SEMPRE BUSCAR OS PERFIS QUANDO BUSCAR O USUARIO
	@JsonProperty(access = Access.WRITE_ONLY)
	@CollectionTable(name="user_profile")
	@Column(name="profile", nullable = false)
	private Set<Integer>profiles = new HashSet<>();
	
	
	public Set<ProfileEnum> getProfiles(){
		return this.profiles.stream().map(x -> ProfileEnum.toEnum(x)).collect(Collectors.toSet());
	}
	
	public void addProfile(ProfileEnum profileEnum) {
		this.profiles.add(profileEnum.getCode());
	}
	
	public User() {	
	}
//CONSTRUTOR
	public User(Long id,String username, String password){
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		
	}
	
	//GETTERS IN SETTERS
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@JsonIgnore
	public List<Task> getTasks() {
		return tasks;
	}
	
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	


	
	//coisa nova
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
		
		if(!(obj instanceof User)) 
			return false;
		
		User other = (User) obj;
		if (this.id == null)
			return false;
		else if (!this.id.equals(other.id))
			return false;
		return Objects.equals(this.id,other.id) && Objects.equals(this.username, other.username) && Objects.equals(this.password,other.password);
	}
	
	
	
}
