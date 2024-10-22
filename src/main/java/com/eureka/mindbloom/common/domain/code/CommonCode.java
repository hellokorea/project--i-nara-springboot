package com.eureka.mindbloom.common.domain.code;

import com.eureka.mindbloom.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonCode extends BaseEntity {

    @Id
    @Column(columnDefinition = "char(7)")
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_group")
    private CommonCodeGroup codeGroup;

    private String name;
}
