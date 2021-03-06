/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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
 * @test
 * @bug 6379235
 * @ignore until 6721694 is fixed
 * @run main/othervm -server -Xmx32m -Xms32m -Xss256m StartOOMTest
 * @summary ThreadGroup accounting mistake possible with failure of Thread.start()
 */

import java.util.*;

public class StartOOMTest
{
    public static void main(String[] args) throws Throwable {
        Runnable r = new SleepRunnable();
        ThreadGroup tg = new ThreadGroup("buggy");
        List<Thread> threads = new ArrayList<Thread>();
        Thread failedThread;
        int i = 0;
        for (i = 0; ; i++) {
            Thread t = new Thread(tg, r);
            try {
                t.start();
                threads.add(t);
            } catch (Throwable x) {
                failedThread = t;
                System.out.println(x);
                System.out.println(i);
                break;
            }
        }

        int j = 0;
        for (Thread t : threads)
            t.interrupt();

        while (tg.activeCount() > i/2)
            Thread.yield();
        failedThread.start();
        failedThread.interrupt();

        for (Thread t : threads)
            t.join();
        failedThread.join();

        try {
            Thread.sleep(1000);
        } catch (Throwable ignore) {
        }

        int activeCount = tg.activeCount();
        System.out.println("activeCount = " + activeCount);

        if (activeCount > 0) {
            throw new RuntimeException("Failed: there  should be no active Threads in the group");
        }
    }

    static class SleepRunnable implements Runnable
    {
        public void run() {
            try {
                Thread.sleep(60*1000);
            } catch (Throwable t) {
            }
        }
    }
}
