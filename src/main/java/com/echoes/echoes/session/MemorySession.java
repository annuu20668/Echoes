package com.echoes.echoes.session;

import com.echoes.echoes.model.Memory;

public class MemorySession {

    private static Memory selectedMemory;

    public static void setSelectedMemory(Memory memory) {
        selectedMemory = memory;
    }

    public static Memory getSelectedMemory() {
        return selectedMemory;
    }

    public static void clear() {
        selectedMemory = null;
    }
}