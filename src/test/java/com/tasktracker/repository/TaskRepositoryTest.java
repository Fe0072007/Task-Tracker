package com.tasktracker.repository;

import com.tasktracker.model.Task;
import com.tasktracker.model.Status;
import com.tasktracker.model.Exceptions.PersistenceException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

class TaskRepositoryTest {
    @TempDir Path directory;

    @Test
    void persistsTaskLifecycleAcrossRepositoryInstances() throws Exception {
        TaskRepository repository = new TaskRepository(directory);
        assertTrue(repository.list().isEmpty());
        Task added = repository.add("Estudar Java");
        TaskRepository reopened = new TaskRepository(directory);
        Task loaded = reopened.list().get(0);
        assertEquals(added.getId(), loaded.getId());
        assertEquals("Estudar Java", loaded.getDescription());
        assertEquals(Status.TODO, loaded.getStatus());
        assertEquals(added.getCreatedAt().toInstant(), loaded.getCreatedAt().toInstant());
        reopened.update(added.getId(), "Estudar Maven");
        reopened.update(added.getId(), Status.DONE);
        Task updated = repository.list(Status.DONE).get(0);
        assertEquals("Estudar Maven", updated.getDescription());
        assertTrue(repository.list(Status.TODO).isEmpty());
        assertTrue(reopened.delete(added.getId()).isEmpty());
        assertTrue(repository.list().isEmpty());
    }

    @Test
    void invalidJsonIsReportedAndNeverOverwrittenByAdd() throws IOException {
        Path file = directory.resolve("tasks.json");
        String invalid = "{invalid json";
        Files.writeString(file, invalid);
        TaskRepository repository = new TaskRepository(directory);
        PersistenceException failure = assertThrows(PersistenceException.class, repository::list);
        assertInstanceOf(IOException.class, failure.getCause());
        assertThrows(PersistenceException.class, () -> repository.add("New task"));
        assertEquals(invalid, Files.readString(file));
    }

    @Test
    void directoryCreationFailurePreservesCause() throws IOException {
        Path file = directory.resolve("not-a-directory");
        Files.writeString(file, "existing data");
        TaskRepository repository = new TaskRepository(file);
        PersistenceException failure = assertThrows(PersistenceException.class, repository::list);
        assertInstanceOf(IOException.class, failure.getCause());
        assertEquals("existing data", Files.readString(file));
    }

    @Test
    void serializationFailurePreservesOriginalAndCleansTemporaryFile() throws IOException {
        Path file = directory.resolve("tasks.json");
        Files.writeString(file, "[]");
        Task broken = new Task(1, "Task") {
            @Override public String getDescription() {
                throw new IllegalStateException("Serialization failure");
            }
        };
        TaskRepository repository = new TaskRepository(directory) {
            @Override public List<Task> list() {
                return new ArrayList<>(List.of(broken));
            }
        };
        PersistenceException failure = assertThrows(PersistenceException.class,
            () -> repository.add("New task"));
        assertInstanceOf(IOException.class, failure.getCause());
        assertEquals("[]", Files.readString(file));
        try (var files = Files.list(directory)) {
            assertEquals(List.of(file), files.toList());
        }
    }
}
