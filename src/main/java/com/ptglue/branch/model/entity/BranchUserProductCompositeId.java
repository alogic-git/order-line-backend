package com.ptglue.branch.model.entity;

import com.ptglue.product.model.entity.Product;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class BranchUserProductCompositeId implements Serializable {

    private BranchUserRole branchUserRole;

    private Product product;
}
