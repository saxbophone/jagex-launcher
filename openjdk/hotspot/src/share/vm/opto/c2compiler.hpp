/*
 * Copyright 1999-2008 Sun Microsystems, Inc.  All Rights Reserved.
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

class C2Compiler : public AbstractCompiler {
private:

  static void initialize_runtime();

public:
  // Name
  const char *name() { return "C2"; }

  static volatile int _runtimes;

#ifdef TIERED
  virtual bool is_c2() { return true; };
#endif // TIERED

  // Customization
  bool needs_adapters         () { return true; }
  bool needs_stubs            () { return true; }

  void initialize();

  // Compilation entry point for methods
  void compile_method(ciEnv* env,
                      ciMethod* target,
                      int entry_bci);

  // sentinel value used to trigger backtracking in compile_method().
  static const char* retry_no_subsuming_loads();
  static const char* retry_no_escape_analysis();

  // Print compilation timers and statistics
  void print_timers();
};
