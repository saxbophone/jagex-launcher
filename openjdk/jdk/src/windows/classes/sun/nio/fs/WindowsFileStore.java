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

import java.nio.file.*;
import java.nio.file.attribute.*;
import java.io.IOException;

import static sun.nio.fs.WindowsConstants.*;
import static sun.nio.fs.WindowsNativeDispatcher.*;

/**
 * Windows implementation of FileStore.
 */

class WindowsFileStore
    extends FileStore
{
    private final String root;
    private final VolumeInformation volInfo;
    private final int volType;
    private final String displayName;   // returned by toString

    private WindowsFileStore(String root) throws WindowsException {
        assert root.charAt(root.length()-1) == '\\';
        this.root = root;
        this.volInfo = GetVolumeInformation(root);
        this.volType = GetDriveType(root);

        // file store "display name" is the volume name if available
        String vol = volInfo.volumeName();
        if (vol.length() > 0) {
            this.displayName = vol;
        } else {
            // TBD - should we map all types? Does this need to be localized?
            this.displayName = (volType == DRIVE_REMOVABLE) ? "Removable Disk" : "";
        }
    }

    static WindowsFileStore create(String root, boolean ignoreNotReady)
        throws IOException
    {
        try {
            return new WindowsFileStore(root);
        } catch (WindowsException x) {
            if (ignoreNotReady && x.lastError() == ERROR_NOT_READY)
                return null;
            x.rethrowAsIOException(root);
            return null; // keep compiler happy
        }
    }

    static WindowsFileStore create(WindowsPath file) throws IOException {
        try {
            // if the file is a link then GetVolumePathName returns the
            // volume that the link is on so we need to call it with the
            // final target
            String target;
            if (file.getFileSystem().supportsLinks()) {
                target = WindowsLinkSupport.getFinalPath(file, true);
            } else {
                // file must exist
                WindowsFileAttributes.get(file, true);
                target = file.getPathForWin32Calls();
            }
            String root = GetVolumePathName(target);
            return new WindowsFileStore(root);
        } catch (WindowsException x) {
            x.rethrowAsIOException(file);
            return null; // keep compiler happy
        }
    }

    VolumeInformation volumeInformation() {
        return volInfo;
    }

    int volumeType() {
        return volType;
    }

    @Override
    public String name() {
        return volInfo.volumeName();   // "SYSTEM", "DVD-RW", ...
    }

    @Override
    public String type() {
        return volInfo.fileSystemName();  // "FAT", "NTFS", ...
    }

    @Override
    public boolean isReadOnly() {
        return ((volInfo.flags() & FILE_READ_ONLY_VOLUME) != 0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V extends FileStoreAttributeView> V getFileStoreAttributeView(Class<V> type) {
        if (type == null)
            throw new NullPointerException();
        if (type == FileStoreSpaceAttributeView.class)
            return (V) new WindowsFileStoreAttributeView(this);
        return (V) null;
    }

    @Override
    public Object getAttribute(String attribute) throws IOException {
        // standard
        if (attribute.equals("space:totalSpace"))
            return new WindowsFileStoreAttributeView(this)
                .readAttributes().totalSpace();
        if (attribute.equals("space:usableSpace"))
            return new WindowsFileStoreAttributeView(this)
                 .readAttributes().usableSpace();
        if (attribute.equals("space:unallocatedSpace"))
            return new WindowsFileStoreAttributeView(this)
                 .readAttributes().unallocatedSpace();
        // windows specific for testing purposes
        if (attribute.equals("volume:vsn"))
            return volInfo.volumeSerialNumber();
        if (attribute.equals("volume:isRemovable"))
            return volType == DRIVE_REMOVABLE;
        if (attribute.equals("volume:isCdrom"))
            return volType == DRIVE_CDROM;
        throw new UnsupportedOperationException("'" + attribute + "' not recognized");
    }

    @Override
    public boolean supportsFileAttributeView(Class<? extends FileAttributeView> type) {
        if (type == null)
            throw new NullPointerException();
        if (type == BasicFileAttributeView.class || type == DosFileAttributeView.class)
            return true;
        if (type == AclFileAttributeView.class || type == FileOwnerAttributeView.class)
            return ((volInfo.flags() & FILE_PERSISTENT_ACLS) != 0);
        if (type == UserDefinedFileAttributeView.class)
            return ((volInfo.flags() & FILE_NAMED_STREAMS) != 0);
        return false;
    }

    @Override
    public boolean supportsFileAttributeView(String name) {
        if (name.equals("basic") || name.equals("dos"))
            return true;
        if (name.equals("acl"))
            return supportsFileAttributeView(AclFileAttributeView.class);
        if (name.equals("owner"))
            return supportsFileAttributeView(FileOwnerAttributeView.class);
        if (name.equals("user"))
            return supportsFileAttributeView(UserDefinedFileAttributeView.class);
        return false;
    }

    @Override
    public boolean equals(Object ob) {
        if (ob == this)
            return true;
        if (!(ob instanceof WindowsFileStore))
            return false;
        WindowsFileStore other = (WindowsFileStore)ob;
        return this.volInfo.volumeSerialNumber() == other.volInfo.volumeSerialNumber();
    }

    @Override
    public int hashCode() {
        // reveals VSN without permission check - okay?
        return volInfo.volumeSerialNumber();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(displayName);
        if (sb.length() > 0)
            sb.append(" ");
        sb.append("(");
        // drop trailing slash
        sb.append(root.subSequence(0, root.length()-1));
        sb.append(")");
        return sb.toString();
    }

    static class WindowsFileStoreAttributeView
        implements FileStoreSpaceAttributeView
    {
        private final WindowsFileStore fs;

        WindowsFileStoreAttributeView(WindowsFileStore fs) {
            this.fs = fs;
        }

        @Override
        public String name() {
            return "space";
        }

        @Override
        public FileStoreSpaceAttributes readAttributes()
            throws IOException
        {
            // read the free space info
            DiskFreeSpace info = null;
            try {
                info = GetDiskFreeSpaceEx(fs.root);
            } catch (WindowsException x) {
                x.rethrowAsIOException(fs.root);
            }

            final DiskFreeSpace result = info;
            return new FileStoreSpaceAttributes() {
                @Override
                public long totalSpace() {
                    return result.totalNumberOfBytes();
                }
                @Override
                public long usableSpace() {
                    return result.freeBytesAvailable();
                }
                @Override
                public long unallocatedSpace() {
                    return result.totalNumberOfFreeBytes();
                }
            };
        }
    }
 }
