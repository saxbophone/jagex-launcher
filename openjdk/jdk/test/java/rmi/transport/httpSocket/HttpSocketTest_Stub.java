/*
 * Copyright 1999-2008 Sun Microsystems, Inc.  All Rights Reserved.
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

// Stub class generated by rmic, do not edit.
// Contents subject to change without notice.

public final class HttpSocketTest_Stub
    extends java.rmi.server.RemoteStub
    implements MyRemoteInterface, java.rmi.Remote
{
    private static final java.rmi.server.Operation[] operations = {
        new java.rmi.server.Operation("java.rmi.Remote getRemoteObject()"),
        new java.rmi.server.Operation("void setRemoteObject(java.rmi.Remote)")
    };

    private static final long interfaceHash = 3775375480010579665L;

    private static final long serialVersionUID = 2;

    private static boolean useNewInvoke;
    private static java.lang.reflect.Method $method_getRemoteObject_0;
    private static java.lang.reflect.Method $method_setRemoteObject_1;

    static {
        try {
            java.rmi.server.RemoteRef.class.getMethod("invoke",
                new java.lang.Class[] {
                    java.rmi.Remote.class,
                    java.lang.reflect.Method.class,
                    java.lang.Object[].class,
                    long.class
                });
            useNewInvoke = true;
            $method_getRemoteObject_0 = MyRemoteInterface.class.getMethod("getRemoteObject", new java.lang.Class[] {});
            $method_setRemoteObject_1 = MyRemoteInterface.class.getMethod("setRemoteObject", new java.lang.Class[] {java.rmi.Remote.class});
        } catch (java.lang.NoSuchMethodException e) {
            useNewInvoke = false;
        }
    }

    // constructors
    public HttpSocketTest_Stub() {
        super();
    }
    public HttpSocketTest_Stub(java.rmi.server.RemoteRef ref) {
        super(ref);
    }

    // methods from remote interfaces

    // implementation of getRemoteObject()
    public java.rmi.Remote getRemoteObject()
        throws java.rmi.RemoteException
    {
        try {
            if (useNewInvoke) {
                Object $result = ref.invoke(this, $method_getRemoteObject_0, null, -2578437860804964265L);
                return ((java.rmi.Remote) $result);
            } else {
                java.rmi.server.RemoteCall call = ref.newCall((java.rmi.server.RemoteObject) this, operations, 0, interfaceHash);
                ref.invoke(call);
                java.rmi.Remote $result;
                try {
                    java.io.ObjectInput in = call.getInputStream();
                    $result = (java.rmi.Remote) in.readObject();
                } catch (java.io.IOException e) {
                    throw new java.rmi.UnmarshalException("error unmarshalling return", e);
                } catch (java.lang.ClassNotFoundException e) {
                    throw new java.rmi.UnmarshalException("error unmarshalling return", e);
                } finally {
                    ref.done(call);
                }
                return $result;
            }
        } catch (java.lang.RuntimeException e) {
            throw e;
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new java.rmi.UnexpectedException("undeclared checked exception", e);
        }
    }

    // implementation of setRemoteObject(Remote)
    public void setRemoteObject(java.rmi.Remote $param_Remote_1)
        throws java.rmi.RemoteException
    {
        try {
            if (useNewInvoke) {
                ref.invoke(this, $method_setRemoteObject_1, new java.lang.Object[] {$param_Remote_1}, -7518632118115022871L);
            } else {
                java.rmi.server.RemoteCall call = ref.newCall((java.rmi.server.RemoteObject) this, operations, 1, interfaceHash);
                try {
                    java.io.ObjectOutput out = call.getOutputStream();
                    out.writeObject($param_Remote_1);
                } catch (java.io.IOException e) {
                    throw new java.rmi.MarshalException("error marshalling arguments", e);
                }
                ref.invoke(call);
                ref.done(call);
            }
        } catch (java.lang.RuntimeException e) {
            throw e;
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.Exception e) {
            throw new java.rmi.UnexpectedException("undeclared checked exception", e);
        }
    }
}