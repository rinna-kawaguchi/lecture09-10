package com.example.userapi.mapper;

import com.example.userapi.entity.User;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    @DataSet(value = "datasets/users.yml")
    @Transactional
    void 全てのユーザーが取得できること() {
        assertThat(userMapper.findAll())
            .hasSize(3)
            .contains(
                new User(1, "tanaka", 25),
                new User(2, "suzuki", 30),
                new User(3, "yamada", 35)
            );
    }

    @Test
    @DataSet(value = "datasets/empty.yml")
    @Transactional
    void DBが空の時に空のリストが返されること() {
        assertThat(userMapper.findAll()).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/users.yml")
    @Transactional
    void 指定したIDのユーザーが取得できること() {
        assertThat(userMapper.findById(1))
            .contains(new User(1, "tanaka", 25));
    }

    @Test
    @DataSet(value = "datasets/users.yml")
    @Transactional
    void 指定したIDのユーザーが存在しない時に空のOptionalが返されること() {
        assertThat(userMapper.findById(4)).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/users.yml")
    @Transactional
    void 指定した年齢より上のユーザーが取得できること() {
        assertThat(userMapper.findByAgeGreaterThan(25))
            .hasSize(2)
            .contains(
                new User(2, "suzuki", 30),
                new User(3, "yamada", 35)
            );
    }

    @Test
    @DataSet(value = "datasets/users.yml")
    @Transactional
    void 指定した年齢より上のユーザーが存在しない時に空のリストが返されること() {
        assertThat(userMapper.findByAgeGreaterThan(35)).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/users.yml")
    @ExpectedDataSet(value = "datasets/insert_users.yml", ignoreCols = "id")
    @Transactional
    void ユーザーが登録でき既存のIDより大きい数字のIDが採番されること() {
        User user = new User("takahashi", 40);
        userMapper.createUser(user);
        assertThat(user.getId()).isGreaterThan(3);
    }

    @Test
    @DataSet(value = "datasets/users.yml")
    @ExpectedDataSet(value = "datasets/update_users.yml")
    @Transactional
    void 指定したIDのユーザーが更新できること() {
        userMapper.updateUser(new User(3, "takahashi", 40));
    }

    @Test
    @DataSet(value = "datasets/users.yml")
    @ExpectedDataSet(value = "datasets/delete_users.yml")
    @Transactional
    void 指定したIDのユーザーが削除できること() {
        userMapper.deleteUser(3);
    }
}
