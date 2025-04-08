package domain;

import lombok.Getter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Objects;

/**
 * 사용자 엔티티
 * 시스템의 사용자 정보를 관리하는 핵심 도메인 엔티티입니다.
 */
@Entity
@Table(name = "users")
@Getter
public class User extends BaseTimeEntity {
    
    /** 사용자 고유 식별자 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 사용자 이메일 (유니크 키) */
    @Column(unique = true, nullable = false)
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    /** 사용자 이름 */
    @Column(nullable = false)
    @NotBlank(message = "이름은 필수입니다.")
    @Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하여야 합니다.")
    private String name;

    /** 사용자 비밀번호 */
    @Column(nullable = false)
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;

    /**
     * JPA를 위한 기본 생성자
     */
    protected User() {
    }

    /**
     * 사용자 생성을 위한 생성자
     * @param email 사용자 이메일
     * @param name 사용자 이름
     * @param password 사용자 비밀번호
     */
    private User(String email, String name, String password) {
        validateEmail(email);
        validateName(name);
        validatePassword(password);
        this.email = email;
        this.name = name;
        this.password = password;
    }

    /**
     * 일반 사용자 생성을 위한 정적 팩토리 메서드
     * @param email 사용자 이메일
     * @param name 사용자 이름
     * @param password 사용자 비밀번호
     * @return 생성된 사용자 객체
     * @throws IllegalArgumentException 유효성 검증에 실패한 경우
     */
    public static User createUser(String email, String name, String password) {
        return new User(email, name, password);
    }

    /**
     * 이메일 정보만으로 사용자 생성 (테스트용)
     * @param email 사용자 이메일
     * @return 생성된 사용자 객체
     */
    public static User withEmail(String email) {
        return new User(email, "Default User", "defaultpassword123");
    }

    /**
     * 기존 사용자 정보를 복사하여 새 사용자 생성
     * @param otherUser 복사할 사용자
     * @return 복사된 새 사용자 객체
     */
    public static User copyOf(User otherUser) {
        Objects.requireNonNull(otherUser, "복사할 사용자는 null일 수 없습니다");
        return new User(otherUser.email, otherUser.name, otherUser.password);
    }

    /**
     * 사용자 이름 업데이트
     * @param name 새로운 이름
     * @throws IllegalArgumentException 유효하지 않은 이름인 경우
     */
    public void updateName(String name) {
        validateName(name);
        this.name = name;
    }

    /**
     * 비밀번호 업데이트
     * @param password 새로운 비밀번호
     * @throws IllegalArgumentException 유효하지 않은 비밀번호인 경우
     */
    public void updatePassword(String password) {
        validatePassword(password);
        this.password = password;
    }

    /**
     * 사용자 이메일 검증
     * @param email 검증할 이메일
     * @throws IllegalArgumentException 유효하지 않은 이메일인 경우
     */
    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }
        
        // 간단한 이메일 형식 검증
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다.");
        }
    }

    /**
     * 사용자 이름 검증
     * @param name 검증할 이름
     * @throws IllegalArgumentException 유효하지 않은 이름인 경우
     */
    private void validateName(String name) {
        if (name == null || name.trim().length() < 2 || name.trim().length() > 50) {
            throw new IllegalArgumentException("이름은 2자 이상 50자 이하여야 합니다.");
        }
    }

    /**
     * 비밀번호 검증
     * @param password 검증할 비밀번호
     * @throws IllegalArgumentException 유효하지 않은 비밀번호인 경우
     */
    private void validatePassword(String password) {
        if (password == null || password.trim().length() < 8) {
            throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && 
               Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
} 