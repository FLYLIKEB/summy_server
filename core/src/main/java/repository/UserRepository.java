package repository;

import domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 사용자 레포지토리
 * Spring Data JPA를 사용한 기본적인 CRUD 작업과
 * QueryDsl을 사용한 복잡한 쿼리 작업을 처리합니다.
 */
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    
    /**
     * 이메일로 사용자 존재 여부 확인
     * @param email 확인할 이메일
     * @return 사용자 존재 여부
     */
    boolean existsByEmail(String email);

    /**
     * 이메일로 사용자 조회
     * @param email 조회할 이메일
     * @return 조회된 사용자
     */
    User findByEmail(String email);
} 