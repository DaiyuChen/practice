/*
 * Copyright (c) 2015 Igola Travel Consultant and Services Company Ltd.
 * www.igola.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Igola Travel Consultant and Services Company Ltd. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with Igola Travel Consultant and Services Company Ltd.
 */

package com.captain.practice.domain;

import com.captain.practice.utils.GuidGenerator;
import com.captain.practice.utils.NumberIncrementalUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Derek Zheng
 */
@MappedSuperclass
public abstract class DomainObject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    Long id;

    @Version
    @Column(name = "version", length = 11)
    private int version = 0;


    @Column(name = "guid")
    String guid = GuidGenerator.createGuid();


    @Column(name = "archived", length = 1)
    boolean archived = false;

    @Column(name = "created_datetime")
    protected LocalDateTime createdDateTime = LocalDateTime.now();


    @Column(name = "last_modified_datetime")
    LocalDateTime lastModifiedDateTime;

    @PrePersist
    @PreUpdate
    public void updateLastModifiedDateTime() {
        lastModifiedDateTime = LocalDateTime.now();
    }

    public void archive() {
        archived = true;
    }

    public void unarchive() {
        archived = false;
    }


    public boolean isArchived() {
        return archived;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof DomainObject)) {
            return false;
        }

        DomainObject other = (DomainObject) obj;

        // if the id is missing, return false
        if (guid() == null) {
            return false;
        }

        // equivalence by guid
        return guid().equals(other.guid());
    }

    @Override
    public int hashCode() {
        return guid.hashCode();
    }

    public String guid() {
        return guid;
    }

    public int version() {
        return version;
    }

    public LocalDateTime createdDateTime() {
        return createdDateTime;
    }

    public LocalDateTime lastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public boolean isNew() {
        return id == null;
    }

    public String getID() {
        return NumberIncrementalUtils.fillToFixedSize(id);
    }

    public boolean isPersisted() {
        return id != null;
    }

    public void copyFrom(DomainObject domainObject) {
//        this.id = domainObject.id;
        this.guid = domainObject.guid;
        this.archived = domainObject.archived;
        this.version = domainObject.version;
        this.createdDateTime = domainObject.createdDateTime;
        this.lastModifiedDateTime = domainObject.lastModifiedDateTime;
    }

    public void updateCreatedDateTime(LocalDateTime localDate) {
        this.createdDateTime = localDate;
    }

}