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

#include <windows.h>

#include "jni.h"
#include "jni_util.h"
#include "jlong.h"
#include "nio.h"
#include "nio_util.h"

#include "sun_nio_ch_WindowsAsynchronousFileChannelImpl.h"


JNIEXPORT jint JNICALL
Java_sun_nio_ch_WindowsAsynchronousFileChannelImpl_readFile(JNIEnv* env, jclass this,
    jlong handle, jlong address, jint len, jlong offset, jlong ov)
{
    BOOL res;
    DWORD nread = 0;

    OVERLAPPED* lpOverlapped = (OVERLAPPED*)jlong_to_ptr(ov);
    lpOverlapped->Offset = (DWORD)offset;
    lpOverlapped->OffsetHigh = (DWORD)((long)(offset >> 32));
    lpOverlapped->hEvent = NULL;

    res = ReadFile((HANDLE) jlong_to_ptr(handle),
                   (LPVOID) jlong_to_ptr(address),
                   (DWORD)len,
                   &nread,
                   lpOverlapped);

    if (res == 0) {
        int error = GetLastError();
        if (error == ERROR_IO_PENDING)
            return IOS_UNAVAILABLE;
        if (error == ERROR_HANDLE_EOF)
            return IOS_EOF;
        JNU_ThrowIOExceptionWithLastError(env, "ReadFile failed");
        return IOS_THROWN;
    }

    return (jint)nread;
}

JNIEXPORT jint JNICALL
Java_sun_nio_ch_WindowsAsynchronousFileChannelImpl_writeFile(JNIEnv* env, jclass this,
    jlong handle, jlong address, jint len, jlong offset, jlong ov)
{
    BOOL res;
    DWORD nwritten = 0;

    OVERLAPPED* lpOverlapped = (OVERLAPPED*)jlong_to_ptr(ov);
    lpOverlapped->Offset = (DWORD)offset;
    lpOverlapped->OffsetHigh = (DWORD)((long)(offset >> 32));
    lpOverlapped->hEvent = NULL;

    res = WriteFile((HANDLE)jlong_to_ptr(handle),
                   (LPVOID) jlong_to_ptr(address),
                   (DWORD)len,
                   &nwritten,
                   lpOverlapped);

    if (res == 0) {
        int error = GetLastError();
        if (error == ERROR_IO_PENDING) {
            return IOS_UNAVAILABLE;
        }
        JNU_ThrowIOExceptionWithLastError(env, "WriteFile failed");
        return IOS_THROWN;
    }
    return (jint)nwritten;
}

JNIEXPORT jint JNICALL
Java_sun_nio_ch_WindowsAsynchronousFileChannelImpl_lockFile(JNIEnv *env, jobject this, jlong handle,
                                                            jlong pos, jlong size, jboolean shared, jlong ov)
{
    BOOL res;
    HANDLE h = jlong_to_ptr(handle);
    DWORD lowPos = (DWORD)pos;
    long highPos = (long)(pos >> 32);
    DWORD lowNumBytes = (DWORD)size;
    DWORD highNumBytes = (DWORD)(size >> 32);
    DWORD flags = (shared == JNI_TRUE) ? 0 : LOCKFILE_EXCLUSIVE_LOCK;
    OVERLAPPED* lpOverlapped = (OVERLAPPED*)jlong_to_ptr(ov);

    lpOverlapped->Offset = lowPos;
    lpOverlapped->OffsetHigh = highPos;
    lpOverlapped->hEvent = NULL;

    res = LockFileEx(h, flags, 0, lowNumBytes, highNumBytes, lpOverlapped);
    if (res == 0) {
        int error = GetLastError();
        if (error == ERROR_IO_PENDING) {
            return IOS_UNAVAILABLE;
        }
        JNU_ThrowIOExceptionWithLastError(env, "WriteFile failed");
        return IOS_THROWN;
    }
    return 0;
}

JNIEXPORT void JNICALL
Java_sun_nio_ch_WindowsAsynchronousFileChannelImpl_close0(JNIEnv* env, jclass this,
    jlong handle)
{
    HANDLE h = (HANDLE)jlong_to_ptr(handle);
    CloseHandle(h);
}
