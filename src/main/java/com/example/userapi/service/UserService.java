package com.example.userapi.service;

import com.example.userapi.entity.User;
import com.example.userapi.form.CreateForm;
import java.util.List;

public interface UserService {
    User findById(int id);

    List<User> findByAge(Integer age);

    User createUser(CreateForm form);

    void updateUser(User updateUser);

    void deleteUser(int id);
}
