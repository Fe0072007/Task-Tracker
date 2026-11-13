package com.tasktracker.repository;

import java.nio.file.Path;
import java.util.List;
import com.tasktracker.model.Task;
import com.tasktracker.model.Exceptions.EmptyException;
import com.tasktracker.model.Exceptions.NotFoundException;
import com.tasktracker.model.Status;

public interface ITaskRepository{  

    Path createJSON();

    Task add(String description);

    Task update(long id, Status status) throws NotFoundException, EmptyException;

    Task update(long id, String description) throws EmptyException, NotFoundException;

    List<Task> list();

    List<Task> list(Status status);

    List<Task> delete(long id) throws EmptyException, NotFoundException;

}
