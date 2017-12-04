package com.captain.practice.repository;

import org.hibernate.dialect.MySQL5InnoDBDialect;

/**
 * @author Wujing
 */
public class UTF8MySQL5InnoDBDialect extends MySQL5InnoDBDialect {
    @Override
    public String getTableTypeString() {
        return "ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8";
    }
}
