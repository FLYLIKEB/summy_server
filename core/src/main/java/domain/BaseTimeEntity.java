package domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * 기본 시간 엔티티
 * 모든 엔티티에서 공통으로 사용되는 생성일시, 수정일시를 관리합니다.
 * JPA Auditing 기능을 통해 자동으로 값이 설정됩니다.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    /** 엔티티 생성일시 */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /** 엔티티 수정일시 */
    @LastModifiedDate
    private LocalDateTime updatedAt;
} 