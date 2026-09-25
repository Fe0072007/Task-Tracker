package com.tasktracker.repository;

import java.nio.file.Path;
import java.util.List;
import com.tasktracker.model.Task;
import com.tasktracker.model.Status;

public interface ITaskRepository {  

    Path createJSON();

    Task add(String description);

    Task update(long id, Status status);

    Task update(long id, String description);

    List<Task> list();

    List<Task> list(Status status);

    List<Task> delete(long id);

}
