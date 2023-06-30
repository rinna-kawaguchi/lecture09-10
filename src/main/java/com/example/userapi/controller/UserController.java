package com.example.userapi.controller;

import com.example.userapi.entity.User;
import com.example.userapi.exception.ResourceNotFoundException;
import com.example.userapi.form.CreateForm;
import com.example.userapi.form.UpdateForm;
import com.example.userapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public User selectUserById(@PathVariable("id") int id) {
        return userService.findById(id);
    }

    @GetMapping("/users")
    public List<User> selectUsersByAge(@RequestParam(value = "age", required = false) Integer age) {
        return userService.findByAge(age);
    }

    @PostMapping("/users")
    public ResponseEntity<Map<String, String>> create(
            @RequestBody @Validated CreateForm form, UriComponentsBuilder uriBuilder) {
        User user = userService.createUser(form);
        URI url = uriBuilder
                .path("/users/" + user.getId())
                .build()
                .toUri();
        return ResponseEntity.created(url).body(Map.of("message", "user successfully created"));
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> update(
            @PathVariable("id") int id, @RequestBody UpdateForm form) {
        userService.updateUser(form.convertToUser(id));
        return ResponseEntity.ok(Map.of("message", "user successfully updated"));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "user successfully deleted"));
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoResourceFound(
            ResourceNotFoundException e, HttpServletRequest request) {
        Map<String, String> body = Map.of(
                "timestamp", ZonedDateTime.now().toString(),
                "status", String.valueOf(HttpStatus.NOT_FOUND.value()),
                "error", HttpStatus.NOT_FOUND.getReasonPhrase(),
                "message", e.getMessage(),
                "path", request.getRequestURI());
        return new ResponseEntity(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, String> body = Map.of(
                "timestamp", ZonedDateTime.now().toString(),
                "status", String.valueOf(HttpStatus.BAD_REQUEST.value()),
                "error", HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "message", "Please enter your name and age",
                "path", request.getRequestURI());
        return new ResponseEntity(body, HttpStatus.BAD_REQUEST);
    }
}
