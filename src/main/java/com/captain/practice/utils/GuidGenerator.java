/*
 * Copyright (c) 2016 Tianbao Travel Ltd.
 * www.tianbaotravel.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Tianbao Travel Ltd. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with Tianbao Travel Ltd.
 */
package com.captain.practice.utils;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

/**
 * @author Wujing
 */
public abstract class GuidGenerator {
    private static final TimeBasedGenerator timeBasedGenerator = Generators.timeBasedGenerator(EthernetAddress.constructMulticastAddress());

    public static String createGuid() {
        return timeBasedGenerator.generate().toString();
    }

    public static String cleanUuid() {
        return timeBasedGenerator.generate().toString().replaceAll("-", "");
    }
}
