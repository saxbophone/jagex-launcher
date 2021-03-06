/*
 * Copyright 2002-2005 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

/* @test
 * @bug 4672609 5076965 4739238
 * @summary Test getLocalAddress getLocalPort
 * @library ..
 */

import java.net.*;
import java.nio.*;
import java.nio.channels.*;

public class LocalAddress {
    public static void main(String[] args) throws Exception {
        test1();
    }

    static void test1() throws Exception {
        InetAddress bogus = InetAddress.getByName("0.0.0.0");
        SocketChannel sc = SocketChannel.open();
        InetSocketAddress saddr = new InetSocketAddress(
            InetAddress.getByName(TestUtil.HOST), 23);

        //Test1: connect only
        sc.connect(saddr);
        InetAddress isa = sc.socket().getLocalAddress();
        if (isa == null || isa.equals(bogus))
            throw new RuntimeException("test failed");

        //Test2: bind and connect
        sc = SocketChannel.open();
        sc.socket().bind(new InetSocketAddress(0));
        if (sc.socket().getLocalPort() == 0)
            throw new RuntimeException("test failed");
        sc.socket().connect(saddr);
        isa = sc.socket().getLocalAddress();
        if (isa == null || isa.isAnyLocalAddress())
            throw new RuntimeException("test failed");

    }
}
