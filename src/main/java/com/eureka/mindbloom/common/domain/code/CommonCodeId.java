package com.eureka.mindbloom.common.domain.code;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonCodeId implements Serializable {
    @Column(columnDefinition = "char(2)")
    private String code;
    @Column(columnDefinition = "char(4)")
    private String codeGroup;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommonCodeId codeId1 = (CommonCodeId) o;
        return Objects.equals(getCode(), codeId1.getCode()) && Objects.equals(getCodeGroup(), codeId1.getCodeGroup());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getCode());
        result = 31 * result + Objects.hashCode(getCodeGroup());
        return result;
    }
}

