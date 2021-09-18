package com.greatlearning.jdbc;

public class UserBuilder {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;


    UserBuilder(Integer id){
        this.id = id;
    }

    public UserBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public User createUser(){
       return new User(id, firstName, lastName, email);
    }

}
