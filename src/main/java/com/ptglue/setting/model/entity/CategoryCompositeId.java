package com.ptglue.setting.model.entity;

import com.ptglue.branch.enums.FunctionTypeEnum;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class CategoryCompositeId implements Serializable {

    private FunctionTypeEnum category;

    private Long categoryId;
}
