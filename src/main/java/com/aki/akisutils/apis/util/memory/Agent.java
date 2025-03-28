package com.aki.akisutils.apis.util.memory;

import java.lang.instrument.Instrumentation;

public class Agent {
    private static volatile Instrumentation instrumentation;

    public static void premain(String args, Instrumentation instr) {
        instrumentation = instr;
    }

    protected static Instrumentation getInstrumentation() {
        Instrumentation instr = instrumentation;
        if (instr == null) {
            throw new IllegalStateException("Agent not initted");
        } else {
            return instr;
        }
    }

    private Agent() {
    }
}
