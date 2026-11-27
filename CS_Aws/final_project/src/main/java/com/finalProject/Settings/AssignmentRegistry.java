package com.finalProject.Settings;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class AssignmentRegistry {
    public record Entry(String name, Path path) {}
    private final Map<UUID, Entry> entryMap = new HashMap<>();
    private final ObjectMapper mapper;


    public AssignmentRegistry() {
        this.mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .enable(SerializationFeature.INDENT_OUTPUT);
    }

    public UUID addEntry(String name, Path path) {
        UUID id = UUID.randomUUID();
        entryMap.put(id, new Entry(name, path.toAbsolutePath().normalize()));
        return id;
    }

    public void updateEntry(UUID id, String name, Path path) {
        if (entryMap.containsKey(id)) {
            entryMap.put(id, new Entry(name, path.toAbsolutePath().normalize()));
        } else {
            throw new IllegalArgumentException("No entry found with id: " + id);
        }
    }

    public void deleteEntry(UUID id) {
        if (entryMap.containsKey(id)) {
            entryMap.remove(id);
        } else {
            throw new IllegalArgumentException("No entry found with id: " + id);
        }
    }

    public Map<UUID, Entry> get() {
        return Map.copyOf(entryMap);
    }

    public Entry getEntry(UUID id) {
        if (entryMap.containsKey(id)) {
            return entryMap.get(id);
        } else {
            throw new IllegalArgumentException("No entry found with id: " + id);
        }
    }

    public Optional<Entry> tryGetEntry(UUID id) {
        if (!entryMap.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(entryMap.get(id));
    }

    public Optional<UUID> tryFindAssignmentByPath(Path path) {
        var toFind = path.toAbsolutePath().normalize();
        for (var entry : entryMap.entrySet()) {
            if (entry.getValue().path().equals(toFind)) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }

    public Map<UUID, Entry> load() {
        Path file = getSaveRootPath().resolve("assignments.json");
        if (!Files.exists(file)) {
            return get();
        }

        try {
            Map<UUID, Entry> loadedEntries = mapper.readValue(file.toFile(), mapper.getTypeFactory().constructMapType(HashMap.class, UUID.class, Entry.class));
            entryMap.clear();
            entryMap.putAll(loadedEntries);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load assignment registry", e);
        }
        return get();
    }

    public void save() {
        Path file = getSaveRootPath().resolve("assignments.json");
        try {
            Files.createDirectories(file.getParent());
            mapper.writeValue(file.toFile(), entryMap);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save assignment registry", e);
        }
    }

    private Path getSaveRootPath() {
        String localAppData = System.getenv("LOCALAPPDATA");
        if(localAppData == null || localAppData.isBlank()) {
            localAppData = ".";
        }
        return Path.of(localAppData, "drandall-auto-grader");
    }
}
