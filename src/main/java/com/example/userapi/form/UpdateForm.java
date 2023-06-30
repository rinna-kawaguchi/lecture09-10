package com.example.userapi.form;

import com.example.userapi.entity.User;
import lombok.Getter;

@Getter

public class UpdateForm {

    private String name;

    private Integer age;

    public User convertToUser(int id) {
        User updateUser = new User(id, name, age);
        return updateUser;
    }
}
