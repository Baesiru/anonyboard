package com.example.anonyboard2.dto;


import com.example.anonyboard2.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDto {
    private Long id;
    @NotBlank(message="아이디를 입력해야 합니다.")
    @Size(min=4, max=12, message="아이디는 4글자 이상 12글자 이하이여야 합니다.")
    private String username;
    @NotBlank(message="비밀번호를 입력해야 합니다.")
    @Size(min=8, max=20, message="비밀번호는 8글자 이상, 20글자 이하여야 합니다.")
    @Pattern(regexp="^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[\\W_]).+$",
    message="비밀번호는 영어, 숫자, 특수문자를 모두 입력해야 합니다.")
    private String password;
    @NotBlank(message="이름을 입력해야 합니다.")
    private String name;
    @NotBlank(message="닉네임을 입력해야 합니다.")
    private String nickname;
    @NotBlank(message="이메일을 입력해야 합니다.")
    @Email(message="이메일 형식을 입력해야 합니다.")
    private String email;
    private String created_date;
    private String modified_date;

    public User toEntity(){
        return new User(id, username, password, name, nickname, email, "USER", created_date, modified_date);
    }
}
