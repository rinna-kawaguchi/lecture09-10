package com.example.lecture09task.service;

import com.example.lecture09task.entity.User;
import com.example.lecture09task.exception.ResourceNotFoundException;
import com.example.lecture09task.form.CreateForm;
import com.example.lecture09task.mapper.UserMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User findById(int id) {
        return userMapper.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("This id is not found"));
    }

    public List<User> findByAge(Integer age) {
        if (age != null) {
            return userMapper.findByAgeGreaterThan(age);
        } else {
            return userMapper.findAll();
        }
    }

    @Override
    public User createUser(CreateForm form) {
        User user = new User(form.getName(), form.getAge());
        userMapper.createUser(user);
        return user;
    }

    @Override
    public void updateUser(User updateUser) {
        User user = userMapper.findById(updateUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("This id is not found"));
        if (Objects.isNull(updateUser.getName())) {
            updateUser.setName(user.getName());
        }
        if (Objects.isNull(updateUser.getAge())) {
            updateUser.setAge(user.getAge());
        }
        userMapper.updateUser(updateUser);
    }

    public void deleteUser(int id) {
        userMapper.findById(id).orElseThrow(() -> new ResourceNotFoundException("This id is not found"));
        userMapper.deleteUser(id);
    }
}
