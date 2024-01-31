package com.ptglue.branch.model.entity;

import com.ptglue.klass.model.entity.Klass;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class BranchUserKlassCompositeId implements Serializable {

    private BranchUserRole branchUserRole;

    private Klass klass;

}
