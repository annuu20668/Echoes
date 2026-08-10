package com.echoes.echoes.service;

import com.echoes.echoes.database.MemoryDAO;
import com.echoes.echoes.model.Memory;

import java.util.List;

public class MemoryService {

    private final MemoryDAO memoryDAO = new MemoryDAO();


    // =========================
    // SAVE MEMORY
    // =========================

    public void saveMemory(Memory memory) {

        if (memory.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Memory title cannot be empty."
            );
        }

        if (memory.getStory().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Memory story cannot be empty."
            );
        }

        memoryDAO.insertMemory(memory);
    }


    // =========================
    // UPDATE MEMORY
    // =========================

    public void updateMemory(Memory memory) {

        if (memory.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Memory title cannot be empty."
            );
        }

        if (memory.getStory().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Memory story cannot be empty."
            );
        }

        memoryDAO.updateMemory(memory);
    }


    // =========================
    // DELETE MEMORY
    // =========================

    public boolean deleteMemory(int memoryId) {

        if (memoryId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid memory ID."
            );
        }

        return memoryDAO.deleteMemory(memoryId);
    }


    // =========================
    // GET USER MEMORIES
    // =========================

    public List<Memory> getMemoriesByUser(int userId) {

        return memoryDAO.getMemoriesByUser(userId);
    }
    public List<Memory> getFavoriteMemoriesByUser(int userId) {

        if (userId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid user ID."
            );
        }

        return memoryDAO.getFavoriteMemoriesByUser(userId);
    }


    // =========================
    // GET LATEST MEMORY
    // =========================

    public Memory getLatestMemory(int userId) {

        return memoryDAO.getLatestMemory(userId);
    }
    public List<Memory> searchMemories(
            int userId,
            String keyword) {

        if (userId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid user ID."
            );
        }

        if (keyword == null) {
            keyword = "";
        }

        return memoryDAO.searchMemories(
                userId,
                keyword
        );
    }

    // =========================
    // FAVORITES
    // =========================

    public boolean updateFavoriteStatus(
            int memoryId,
            boolean favorite) {

        if (memoryId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid memory ID."
            );
        }

        return memoryDAO.updateFavoriteStatus(
                memoryId,
                favorite
        );
    }
}