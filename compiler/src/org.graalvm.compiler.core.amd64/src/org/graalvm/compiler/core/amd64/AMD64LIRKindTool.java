/*
 * Copyright (c) 2015, 2018, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
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
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.graalvm.compiler.core.amd64;

import org.graalvm.compiler.core.common.LIRKind;
import org.graalvm.compiler.core.common.spi.LIRKindTool;
import org.graalvm.compiler.debug.GraalError;

import jdk.vm.ci.amd64.AMD64Kind;

public abstract class AMD64LIRKindTool implements LIRKindTool {

    @Override
    public LIRKind getIntegerKind(int bits) {
        return LIRKind.value(getIntegerAMD64Kind(bits));
    }

    private static AMD64Kind getIntegerAMD64Kind(int bits) {
        if (bits <= 8) {
            return AMD64Kind.BYTE;
        } else if (bits <= 16) {
            return AMD64Kind.WORD;
        } else if (bits <= 32) {
            return AMD64Kind.DWORD;
        } else {
            assert bits <= 64;
            return AMD64Kind.QWORD;
        }
    }

    @Override
    public LIRKind getVectorIntegerKind(int bits, int elementCount) {
        final AMD64Kind scalarKind = getIntegerAMD64Kind(bits);
        switch (scalarKind) {
            case BYTE:
                if (elementCount <= 4) {
                    return LIRKind.value(AMD64Kind.V32_BYTE);
                }
                if (elementCount <= 8) {
                    return LIRKind.value(AMD64Kind.V64_BYTE);
                }
                if (elementCount <= 16) {
                    return LIRKind.value(AMD64Kind.V128_BYTE);
                }
                if (elementCount <= 32) {
                    return LIRKind.value(AMD64Kind.V256_BYTE);
                }
                if (elementCount <= 64) {
                    return LIRKind.value(AMD64Kind.V512_BYTE);
                }
                throw GraalError.shouldNotReachHere("vector too large");
            case WORD:
                if (elementCount <= 2) {
                    return LIRKind.value(AMD64Kind.V32_WORD);
                }
                if (elementCount <= 4) {
                    return LIRKind.value(AMD64Kind.V64_WORD);
                }
                if (elementCount <= 8) {
                    return LIRKind.value(AMD64Kind.V128_WORD);
                }
                if (elementCount <= 16) {
                    return LIRKind.value(AMD64Kind.V256_WORD);
                }
                if (elementCount <= 32) {
                    return LIRKind.value(AMD64Kind.V512_WORD);
                }
                throw GraalError.shouldNotReachHere("vector too large");
            case DWORD:
                if (elementCount <= 2) {
                    return LIRKind.value(AMD64Kind.V64_DWORD);
                }
                if (elementCount <= 4) {
                    return LIRKind.value(AMD64Kind.V128_DWORD);
                }
                if (elementCount <= 8) {
                    return LIRKind.value(AMD64Kind.V256_DWORD);
                }
                if (elementCount <= 16) {
                    return LIRKind.value(AMD64Kind.V512_DWORD);
                }
                throw GraalError.shouldNotReachHere("vector too large");
            case QWORD:
                if (elementCount <= 2) {
                    return LIRKind.value(AMD64Kind.V128_QWORD);
                }
                if (elementCount <= 4) {
                    return LIRKind.value(AMD64Kind.V256_QWORD);
                }
                if (elementCount <= 8) {
                    return LIRKind.value(AMD64Kind.V512_QWORD);
                }
                throw GraalError.shouldNotReachHere("vector too large");
        }

        throw GraalError.shouldNotReachHere("vector too large");
    }

    @Override
    public LIRKind getFloatingKind(int bits) {
        return LIRKind.value(getFloatingAMD64Kind(bits));
    }

    private static AMD64Kind getFloatingAMD64Kind(int bits) {
        switch (bits) {
            case 32:
                return AMD64Kind.SINGLE;
            case 64:
                return AMD64Kind.DOUBLE;
            default:
                throw GraalError.shouldNotReachHere("could not find floating type with bit length " + bits);
        }
    }

    @Override
    public LIRKind getVectorFloatingKind(int bits, int elementCount) {
        final AMD64Kind scalarKind = getFloatingAMD64Kind(bits);
        switch (scalarKind) {
            case SINGLE:
                if (elementCount <= 4) {
                    return LIRKind.value(AMD64Kind.V128_SINGLE);
                }
                if (elementCount <= 8) {
                    return LIRKind.value(AMD64Kind.V256_SINGLE);
                }
                if (elementCount <= 16) {
                    return LIRKind.value(AMD64Kind.V512_SINGLE);
                }
                throw GraalError.shouldNotReachHere("vector too large");
            case DOUBLE:
                if (elementCount <= 2) {
                    return LIRKind.value(AMD64Kind.V128_DOUBLE);
                }
                if (elementCount <= 4) {
                    return LIRKind.value(AMD64Kind.V256_DOUBLE);
                }
                if (elementCount <= 8) {
                    return LIRKind.value(AMD64Kind.V512_DOUBLE);
                }
                throw GraalError.shouldNotReachHere("vector too large");
        }

        throw GraalError.shouldNotReachHere("vector too large");
    }

    @Override
    public LIRKind getObjectKind() {
        return LIRKind.reference(AMD64Kind.QWORD);
    }

    @Override
    public LIRKind getWordKind() {
        return LIRKind.value(AMD64Kind.QWORD);
    }

    @Override
    public abstract LIRKind getNarrowOopKind();

    @Override
    public abstract LIRKind getNarrowPointerKind();
}
