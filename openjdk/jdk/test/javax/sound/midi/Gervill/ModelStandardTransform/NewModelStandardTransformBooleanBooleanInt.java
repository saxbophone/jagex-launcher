/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
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

/* @test
   @summary Test ModelStandardTransform(boolean,boolean,int) constructor */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.*;

import com.sun.media.sound.*;

public class NewModelStandardTransformBooleanBooleanInt {

    public static void main(String[] args) throws Exception {
        ModelStandardTransform transform = new ModelStandardTransform(
                ModelStandardTransform.DIRECTION_MAX2MIN,
                ModelStandardTransform.POLARITY_BIPOLAR,
                ModelStandardTransform.TRANSFORM_CONVEX);
        if(transform.getDirection() != ModelStandardTransform.DIRECTION_MAX2MIN)
            throw new RuntimeException("transform.getDirection() doesn't return ModelStandardTransform.DIRECTION_MAX2MIN!");
        if(transform.getPolarity() != ModelStandardTransform.POLARITY_BIPOLAR)
            throw new RuntimeException("transform.getPolarity() doesn't return ModelStandardTransform.POLARITY_BIPOLAR!");
        if(transform.getTransform() != ModelStandardTransform.TRANSFORM_CONVEX)
            throw new RuntimeException("transform.getTransform() doesn't return ModelStandardTransform.TRANSFORM_CONVEX!");
    }
}
