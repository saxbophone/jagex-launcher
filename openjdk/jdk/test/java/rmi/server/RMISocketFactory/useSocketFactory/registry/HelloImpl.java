/*
 * Copyright 1998-2008 Sun Microsystems, Inc.  All Rights Reserved.
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

/*
 */

import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class HelloImpl
    extends UnicastRemoteObject
    implements Hello
{

    public static boolean clientCalledSuccessfully = false;

    public HelloImpl() throws RemoteException {
        super(0);
    }

    public synchronized String sayHello() {
        HelloImpl.clientCalledSuccessfully = true;
        System.out.println("hello method called");
        this.notifyAll();
        return "hello";
    }

    public static void main(String[] args) {
        /*
         * The following line is required with the JDK 1.2 VM so that the
         * VM can exit gracefully when this test completes.  Otherwise, the
         * conservative garbage collector will find a handle to the server
         * object on the native stack and not clear the weak reference to
         * it in the RMI runtime's object table.
         */
        Object dummy = new Object();
        Hello hello = null;
        Registry registry = null;

        TestLibrary.suggestSecurityManager("java.rmi.RMISecurityManager");

        try {
            String protocol = "";
            if (args.length >= 1)
                protocol = args[0];

            registry = java.rmi.registry.LocateRegistry.
                getRegistry("localhost", TestLibrary.REGISTRY_PORT,
                            new Compress.CompressRMIClientSocketFactory());
            UseCustomSocketFactory.checkStub(registry, "RMIClientSocket");
            hello = (Hello) registry.lookup("/HelloServer");

            /* lookup server */
            System.err.println(hello.sayHello() +
                               ", remote greeting.");
        } catch (Exception e) {
            System.err.println("EXCEPTION OCCURRED:");
            e.printStackTrace();
        } finally {
            hello = null;
        }
    }
}
