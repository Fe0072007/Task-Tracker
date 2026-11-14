package com.tasktracker.services;

import com.tasktracker.model.Exceptions.NotFoundException;
import com.tasktracker.model.Exceptions.PersistenceException;
import com.tasktracker.repository.ITaskRepository;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {
    private ITaskRepository repository(java.lang.reflect.InvocationHandler handler) {
        return (ITaskRepository) Proxy.newProxyInstance(
            ITaskRepository.class.getClassLoader(),
            new Class<?>[] {ITaskRepository.class}, handler);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t\n"})
    void rejectsInvalidDescriptionsBeforeAccessingRepository(String description) {
        TaskService service = new TaskService(repository((proxy, method, args) -> {
            throw new AssertionError("Repository must not be called");
        }));
        assertNull(service.Add(description));
        assertNull(service.Update(1, description));
    }

    @Test
    void deletingLastTaskIsSuccessful() {
        TaskService service = new TaskService(repository((proxy, method, args) -> {
            assertEquals("delete", method.getName());
            assertEquals(1L, args[0]);
            return List.of();
        }));
        assertTrue(service.Delete(1));
    }

    @Test
    void handlesMissingTasks() {
        TaskService service = new TaskService(repository((proxy, method, args) -> {
            throw new NotFoundException("Task not found");
        }));
        assertNull(service.Update(99, "Updated"));
        assertNull(service.MarkProgress(99));
        assertNull(service.MarkDone(99));
        assertFalse(service.Delete(99));
    }

    @Test
    void propagatesPersistenceFailureWithOriginalCause() {
        IOException cause = new IOException("Disk failure");
        PersistenceException failure = new PersistenceException("Cannot save", cause);
        TaskService service = new TaskService(repository((proxy, method, args) -> {
            throw failure;
        }));
        assertSame(failure, assertThrows(PersistenceException.class,
            () -> service.Add("Task")));
        assertSame(cause, failure.getCause());
    }
}
