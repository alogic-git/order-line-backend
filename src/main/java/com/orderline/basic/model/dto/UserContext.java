package com.orderline.basic.model.dto;

import com.orderline.common.user.enums.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserContext {
    private final Long userId;
    private final Long branchId;
    private final UserRoleEnum role;
}
