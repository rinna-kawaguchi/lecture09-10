package com.example.userapi.service;

import com.example.userapi.entity.User;
import com.example.userapi.exception.ResourceNotFoundException;
import com.example.userapi.form.CreateForm;
import com.example.userapi.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Mock
    UserMapper userMapper;

    @Test
    public void 存在するIDを指定した時に正常にユーザーが返されること() {
        doReturn(Optional.of(new User(1, "tanaka", 25))).when(userMapper).findById(1);

        User actual = userServiceImpl.findById(1);
        assertThat(actual).isEqualTo(new User(1, "tanaka", 25));
        verify(userMapper, times(1)).findById(1);
    }

    @Test
    public void 存在しないIDを指定した時に例外がスローされること() {
        doReturn(Optional.empty()).when(userMapper).findById(99);

        assertThatThrownBy(() -> userServiceImpl.findById(99))
                .isInstanceOfSatisfying(ResourceNotFoundException.class, e -> {
                    assertThat(e.getMessage()).isEqualTo( "This id is not found");
                });
        verify(userMapper, times(1)).findById(99);
    }

    @Test
    public void 指定した年齢以上のユーザーが返されること() {
        List<User> user = List.of(new User(3, "yamada", 35));
        doReturn(user).when(userMapper).findByAgeGreaterThan(30);

        List<User> actual = userServiceImpl.findByAge(30);
        assertThat(actual).isEqualTo(user);
        verify(userMapper, times(1)).findByAgeGreaterThan(30);
        verify(userMapper,times(0)).findAll();
    }

    @Test
    public void 指定した年齢以上のユーザーが存在しない時に空のリストが返されること() {
        List<User> emptyList = new ArrayList<>();
        doReturn(emptyList).when(userMapper).findByAgeGreaterThan(40);
        List<User> actual = userServiceImpl.findByAge(40);
        assertThat(actual).isEqualTo(emptyList);

        verify(userMapper, times(1)).findByAgeGreaterThan(40);
        verify(userMapper,times(0)).findAll();
    }

    @Test
    public void リクエストで年齢の指定がない時に全てのユーザーが返されること() {
        List<User> user = List.of(
                new User(1, "tanaka", 25),
                new User(2, "suzuki", 30),
                new User(3, "yamada", 35));
        doReturn(user).when(userMapper).findAll();

        List<User> actual = userServiceImpl.findByAge(null);
        assertThat(actual).isEqualTo(user);
        verify(userMapper, never()).findByAgeGreaterThan(30);
        verify(userMapper,times(1)).findAll();
    }

    @Test
    public void ユーザーが登録できること() {
        CreateForm form = new CreateForm("takahashi", 40);
        User user = new User(form.getName(), form.getAge());
        doNothing().when(userMapper).createUser(user);
        userServiceImpl.createUser(form);
        verify(userMapper, times(1)).createUser(user);
    }

    @Test
    public void 存在するIDのユーザーが更新できること() {
        User updateUser = new User(1, "sato", 27);
        doReturn(Optional.of(new User(1, "tanaka", 25))).when(userMapper).findById(1);

        userServiceImpl.updateUser(updateUser);
        verify(userMapper, times(1)).findById(1);
        verify(userMapper, times(1)).updateUser(updateUser);
    }

    @Test
    public void 更新対象のIDが存在しない時に例外がスローされること() {
        User updateUser = new User(99, "sato", 27);
        doReturn(Optional.empty()).when(userMapper).findById(updateUser.getId());

        assertThatThrownBy(() -> userServiceImpl.updateUser(updateUser))
                .isInstanceOfSatisfying(ResourceNotFoundException.class, e -> {
                    assertThat(e.getMessage()).isEqualTo( "This id is not found");
                });
        verify(userMapper, times(1)).findById(updateUser.getId());
        verify(userMapper, never()).updateUser(updateUser);
    }

    @Test
    public void 更新リクエストの名前がnullの時に登録済みの名前でユーザーが更新できること() {
        User updateUser = new User(1, null, 27);
        doReturn(Optional.of(new User(1, "tanaka", 25))).when(userMapper).findById(1);

        userServiceImpl.updateUser(updateUser);
        assertThat(updateUser.getName()).isEqualTo("tanaka");
    }

    @Test
    public void 更新リクエストの年齢がnullの時に登録済みの年齢でユーザーが更新できること() {
        User updateUser = new User(1, "sato", null);
        doReturn(Optional.of(new User(1, "tanaka", 25))).when(userMapper).findById(1);

        userServiceImpl.updateUser(updateUser);
        assertThat(updateUser.getAge()).isEqualTo(25);
    }

    @Test
    public void 存在するIDのユーザーが削除できること() {
        doReturn(Optional.of(new User(1, "tanaka", 25))).when(userMapper).findById(1);

        userServiceImpl.deleteUser(1);
        verify(userMapper, times(1)).findById(1);
        verify(userMapper, times(1)).deleteUser(1);
    }

    @Test
    public void 更新対象のIDが存在しない場合例外がスローされること() {
        doReturn(Optional.empty()).when(userMapper).findById(99);

        assertThatThrownBy(() -> userServiceImpl.deleteUser(99))
                .isInstanceOfSatisfying(ResourceNotFoundException.class, e -> {
                    assertThat(e.getMessage()).isEqualTo( "This id is not found");
                });
        verify(userMapper, times(1)).findById(99);
        verify(userMapper, never()).deleteUser(99);

    }
}
