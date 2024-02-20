package com.orderline.basic.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserContext {
    private final Long userId;
    private final Long branchId;
}
