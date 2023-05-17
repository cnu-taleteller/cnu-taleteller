package com.cnu.taleteller.backend.domain.user.entity;
// BaseTimeEntity
// 그 외 등록자, 수정자 관련 애너테이션 지원(필요시 해당 엔티티 클래스 상속)

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class}) // Auditing 적용을위해서
@MappedSuperclass // 자식클래스에 매핑정보만 전달함
@Getter
@Setter
public abstract class BaseTimeEntity {

    @CreatedDate // 엔티티 생성후 저장시 시간자동저장
    @Column(updatable = false)
    private LocalDateTime payDate;

    //@LastModifiedDate // 엔티티 값 변경시 시간 자동 저장
    //private LocalDateTime updated_at;

}
