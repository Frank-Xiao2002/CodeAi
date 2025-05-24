package top.frankxxj.codeai.midware.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "app_user")
@ToString(of = {"id", "name"})
@NoArgsConstructor
public class AppUser {

    public static final String ROLE_SPLIT = ",";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "password")
    private String pwd;

    @Column(name = "roles")
    private String roles;

    @Column(name = "create_at", updatable = false, nullable = false)
    private LocalDateTime createAt;

    public AppUser(String name, String pwd) {
        this.name = name;
        this.pwd = pwd;
        this.createAt = LocalDateTime.now();
        this.roles = "USER";
    }

    public AppUser(String name, String pwd, String roles) {
        this.name = name;
        this.pwd = pwd;
        this.roles = roles;
        this.createAt = LocalDateTime.now();
    }
}