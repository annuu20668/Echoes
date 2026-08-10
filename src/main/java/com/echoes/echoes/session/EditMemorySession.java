package com.echoes.echoes.session;

import com.echoes.echoes.model.Memory;

public class EditMemorySession {

    private static boolean editing = false;

    private static Memory memory;

    public static void startEditing(Memory selectedMemory) {
        editing = true;
        memory = selectedMemory;
    }

    public static boolean isEditing() {
        return editing;
    }

    public static Memory getMemory() {
        return memory;
    }

    public static void clear() {
        editing = false;
        memory = null;
    }
}