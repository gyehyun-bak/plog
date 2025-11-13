package com.example.server.global.auth.validation;

import com.example.server.domain.user.entity.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * <p> {@link User} 필드 중 {@code username}의 유효성을 검증합니다.
 *
 * <p> 다음 규칙을 따릅니다:
 * <ul>
 *     <li>영문 대소문자, 숫자, 밑줄(_)만 사용할 수 있습니다.</li>
 *     <li>길이는 1자 이상 15자 이하로 제한됩니다.</li>
 *     <li>공백이나 기타 특수문자는 사용할 수 없습니다.</li>
 * </ul>
 *
 * <p> 검증에 실패하면 {@code false}를 반환하고 {@link ConstraintViolationException}이 던져집니다. 정해진 메시지 {@code message}가 Validation 예외 메시지로 반환됩니다.
 */
@Component
public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{1,15}$");
    private static final String message = "사용자 이름은 1자 이상 15자 이하의 영문자, 숫자, 밑줄(_)만 사용할 수 있습니다.";

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        // null 값은 다른 @NotBlank 등의 어노테이션에서 검증하도록 허용
        if (username == null || username.isBlank()) {
            return true;
        }

        // 형식 검증
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
