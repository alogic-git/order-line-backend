package com.ptglue.basic.model.dto;

import com.ptglue.common.user.enums.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserContext {
    private final Long userId;
    private final Long branchId;
    private final UserRoleEnum role;
}
