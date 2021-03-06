/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
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

package sun.nio.fs;

import java.nio.ByteBuffer;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.io.IOException;
import java.util.*;

/**
 * Base implementation of UserDefinedAttributeView
 */

abstract class AbstractUserDefinedFileAttributeView
    implements UserDefinedFileAttributeView, DynamicFileAttributeView
{
    protected AbstractUserDefinedFileAttributeView() { }

    protected void checkAccess(String file,
                               boolean checkRead,
                               boolean checkWrite)
    {
        assert checkRead || checkWrite;
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            if (checkRead)
                sm.checkRead(file);
            if (checkWrite)
                sm.checkWrite(file);
            sm.checkPermission(new RuntimePermission("accessUserDefinedAttributes"));
        }
    }

    @Override
    public final String name() {
        return "user";
    }

    @Override
    public final Object getAttribute(String attribute) throws IOException {
        int size;
        try {
            size = size(attribute);
        } catch (IOException e) {
            // not found or some other I/O error
            if (list().contains(attribute))
                throw e;
            return null;
        }

        byte[] buf = new byte[size];
        int n = read(attribute, ByteBuffer.wrap(buf));
        return (n == size) ? buf : Arrays.copyOf(buf, n);
    }

    @Override
    public final void setAttribute(String attribute, Object value)
        throws IOException
    {
        ByteBuffer bb;
        if (value instanceof byte[]) {
            bb = ByteBuffer.wrap((byte[])value);
        } else {
            bb = (ByteBuffer)value;
        }
        write(attribute, bb);
    }

    @Override
    public final Map<String,?> readAttributes(String[] attributes)
        throws IOException
    {
        // names of attributes to return
        List<String> names = new ArrayList<String>();

        for (String name: attributes) {
            if (name.equals("*")) {
                names = list();
                break;
            } else {
                names.add(name);
            }
        }

        // read each value and return in map
        Map<String,Object> result = new HashMap<String,Object>();
        for (String name: names) {
            Object value = getAttribute(name);
            if (value != null)
                result.put(name, value);
        }

        return result;
    }
}
