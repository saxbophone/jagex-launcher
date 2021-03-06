/*
 * Copyright 2002-2003 Sun Microsystems, Inc.  All Rights Reserved.
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
 *
 */

package sun.jvm.hotspot.asm.x86;

import sun.jvm.hotspot.asm.Address;
import sun.jvm.hotspot.asm.DirectAddress;
import sun.jvm.hotspot.asm.Register;

public class X86DirectAddress extends DirectAddress {
   private long segment;
   public X86DirectAddress(long segment, long disp) {
      super(disp);
      this.segment = segment;
   }
   public X86DirectAddress(long disp) {
      super(disp);
      this.segment = 0;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      if (getSegment() != 0) {
         buf.append("0x");
         buf.append(Long.toHexString(getSegment()));
         buf.append(":");
      }
      buf.append("[");
      buf.append("0x");
      buf.append(Long.toHexString(getValue()));
      buf.append("]");

      return buf.toString();
   }

   long getSegment() {
      return segment;
   }
}
