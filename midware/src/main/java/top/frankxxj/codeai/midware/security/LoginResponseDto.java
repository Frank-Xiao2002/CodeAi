package top.frankxxj.codeai.midware.security;

public record LoginResponseDto(Long id,
                               String token,
                               String roles) {
}
