package com.example.userapi.mapper;

import com.example.userapi.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users")
    List<User> findAll();

    @Select("SELECT * FROM users WHERE id = #{id}")
    Optional<User> findById(int id);

    @Select("SELECT * FROM users WHERE age > #{age}")
    List<User> findByAgeGreaterThan(int age);

    @Insert("INSERT INTO users (name, age) VALUES (#{name}, #{age})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void createUser(User user);

    @Update("UPDATE users SET name = #{name}, age = #{age} where id = #{id}")
    void updateUser(User updateUser);

    @Delete("DELETE FROM users WHERE id = #{id}")
    void deleteUser(int id);
}
