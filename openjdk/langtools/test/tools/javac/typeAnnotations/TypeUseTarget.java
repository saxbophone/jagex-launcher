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
 * @summary check that type annotations may appear on all type declarations
 * @author Mahmood Ali
 * @compile -source 1.7 TypeUseTarget.java
 */

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@A
class TypeUseTarget<K extends @A Object> {
  @A String @A [] field;

  @A String test(@A String param, @A String @A ... vararg) @A {
    @A Object o = new @A String @A [3];
    TypeUseTarget<@A String> target;
    return (@A String) null;
  }

  <K> @A String genericMethod(K k) { return null; }
}

@A
interface MyInterface { }

@A
@interface MyAnnotation { }

@Target(ElementType.TYPE_USE)
@interface A { }
