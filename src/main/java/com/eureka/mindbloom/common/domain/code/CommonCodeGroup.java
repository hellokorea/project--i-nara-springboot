package com.eureka.mindbloom.common.domain.code;

import com.eureka.mindbloom.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonCodeGroup extends BaseEntity {

    @Id
    @Column(columnDefinition = "char(4)")
    private String codeGroup;

    private String groupName;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private CommonCodeGroup parent;

    private boolean disabled = false;
}