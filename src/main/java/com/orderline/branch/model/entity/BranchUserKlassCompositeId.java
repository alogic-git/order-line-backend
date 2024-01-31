package com.orderline.branch.model.entity;

import com.orderline.klass.model.entity.Klass;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class BranchUserKlassCompositeId implements Serializable {

    private BranchUserRole branchUserRole;

    private Klass klass;

}
