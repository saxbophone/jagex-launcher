/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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

import java.nio.file.*;
import java.nio.file.attribute.*;
import java.io.IOException;
import java.util.*;

/**
 * Invokes Files.walkFileTree to traverse a file tree and prints
 * each of the directories and files. The -L option causes symbolic
 * links to be followed.
 */

public class PrintFileTree {

    public static void main(String[] args) throws Exception {
        boolean followLinks = false;
        Path dir;

        if (args[0].equals("-L")) {
            followLinks = true;
            dir = Paths.get(args[1]);
        } else {
            dir = Paths.get(args[0]);
        }

        Set<FileVisitOption> options = new HashSet<FileVisitOption>();
        if (followLinks)
            options.add(FileVisitOption.FOLLOW_LINKS);

        Files.walkFileTree(dir, options, Integer.MAX_VALUE, new FileVisitor<FileRef>() {
            public FileVisitResult preVisitDirectory(FileRef dir) {
                System.out.println(dir);
                return FileVisitResult.CONTINUE;
            }
            public FileVisitResult preVisitDirectoryFailed(FileRef dir, IOException exc) {
                exc.printStackTrace();
                return FileVisitResult.CONTINUE;
            }
            public FileVisitResult visitFile(FileRef file, BasicFileAttributes attrs) {
                System.out.println(file);
                return FileVisitResult.CONTINUE;
            }
            public FileVisitResult postVisitDirectory(FileRef dir, IOException exc) {
                if (exc != null) {
                    exc.printStackTrace();
                    return FileVisitResult.TERMINATE;
                }
                return FileVisitResult.CONTINUE;
            }
            public FileVisitResult visitFileFailed(FileRef file, IOException exc) {
                exc.printStackTrace();
                return FileVisitResult.TERMINATE;
            }
        });
    }
}
