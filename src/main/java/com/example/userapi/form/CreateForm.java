package com.example.userapi.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateForm {

    @NotBlank
    private String name;

    @NotNull
    private Integer age;

}
