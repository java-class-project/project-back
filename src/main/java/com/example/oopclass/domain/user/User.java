package com.example.oopclass.domain.user;

import com.example.oopclass.domain.major.Major;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "user_uuid", updatable = false)
    private UUID userUuid;

    @Column(name = "username")
    private String username;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "password")
    private String password;

    @Column(name = "student_number", unique = true)
    private String studentNumber;

    @ManyToOne
    @JoinColumn(name = "main_major_uuid", nullable = false)
    private Major mainMajor;

    @ManyToOne
    @JoinColumn(name = "sub_major1_uuid")
    private Major subMajor1;

    @ManyToOne
    @JoinColumn(name = "sub_major2_uuid")
    private Major subMajor2;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    public UUID getUserUuid() {
        return userUuid;
    }
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
