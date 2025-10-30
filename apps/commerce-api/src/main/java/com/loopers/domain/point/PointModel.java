package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Table(name = "point")
@Entity
public class PointModel extends BaseEntity {
    private static final long INITIAL_POINT = 0L;

    private Long point;

    @Column(name = "user_id", nullable = false)
    private Long userModelId;

    public static PointModel create(final Long userModelId) {
        PointModel pointModel = new PointModel();
        pointModel.point = INITIAL_POINT;
        pointModel.userModelId = userModelId;
        return pointModel;
    }
}
