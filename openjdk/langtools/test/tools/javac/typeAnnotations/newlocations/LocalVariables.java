/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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
 * @bug 6843077
 * @summary new type annotation location: local variables array/generics
 * @author Mahmood Ali
 * @compile -source 1.7 LocalVariables.java
 */

class DefaultScope {
  void parameterized() {
    Parameterized<String, String> unannotated;
    Parameterized<@A String, String> firstTypeArg;
    Parameterized<String, @A String> secondTypeArg;
    Parameterized<@A String, @B String> bothTypeArgs;

    Parameterized<@A Parameterized<@A String, @B String>, @B String>
      nestedParameterized;
  }

  void arrays() {
    @A String [] array1;
    @A String @B [] array1Deep;
    @A String [] [] array2;
    @A String @A [] @B [] array2Deep;
    String @A [] [] array2First;
    String [] @B [] array2Second;
  }
}

class ModifiedVars {
  void parameterized() {
    final Parameterized<String, String> unannotated = null;
    final Parameterized<@A String, String> firstTypeArg = null;
    final Parameterized<String, @A String> secondTypeArg = null;
    final Parameterized<@A String, @B String> bothTypeArgs = null;

    final Parameterized<@A Parameterized<@A String, @B String>, @B String>
      nestedParameterized = null;
  }

  void arrays() {
    final @A String [] array1 = null;
    final @A String @B [] array1Deep = null;
    final @A String [] [] array2 = null;
    final @A String @A [] @B [] array2Deep = null;
    final String @A [] [] array2First = null;
    final String [] @B [] array2Second = null;
  }
}

class Parameterized<K, V> { }

@interface A { }
@interface B { }
