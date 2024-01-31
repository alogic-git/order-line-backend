package com.orderline.setting.model.entity;

import com.orderline.branch.enums.FunctionTypeEnum;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class CategoryCompositeId implements Serializable {

    private FunctionTypeEnum category;

    private Long categoryId;
}
