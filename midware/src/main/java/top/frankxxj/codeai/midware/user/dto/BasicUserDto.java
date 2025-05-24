package top.frankxxj.codeai.midware.user.dto;

import java.io.Serializable;

/**
 * DTO for {@link top.frankxxj.codeai.midware.user.AppUser}
 */
public record BasicUserDto(String name,
                           String pwd) implements Serializable {
}