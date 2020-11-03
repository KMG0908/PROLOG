package com.spring.prolog.dto;

public class UserDTO {
	public String email;
	public String passwd;
	public String lastName;//성
	public String firstName; //이름
	
	public UserDTO() {}

	public UserDTO(String email, String passwd, String lastName, String firstName) {
		super();
		this.email = email;
		this.passwd = passwd;
		this.lastName = lastName;
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
}
