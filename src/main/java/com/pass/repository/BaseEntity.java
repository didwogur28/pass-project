package com.pass.repository;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 공통 매핑 정보를 가진 Entity
 */
@MappedSuperclass // 상속받은 entity에서 아래 필드들을 컬럼으로 사용할 수 있음
@EntityListeners(AuditingEntityListener.class) // Auditing 정보를 캡처하는 Listener
public abstract class BaseEntity {

    @CreatedDate // 생성 일시를 생성
    @Column(updatable = false, nullable = false) // 업데이트를 하지 않도록, null이 되지 않도록 명시
    private LocalDateTime createdAt;

    @LastModifiedDate // 마지막 수정 일시를 생성
    private LocalDateTime modifiedAt;
}
