package com.tasktracker.services;

import com.tasktracker.model.Task;
import java.util.List;


public interface ITaskService {

    Task Add(String description);

    Task Update(long id, String description);

    boolean Delete(long id);

    Task MarkProgress(long id);

    Task MarkDone(long id);

    List<Task> List();

    List<Task> ListDone();

    List<Task> ListTodo();

    List<Task> ListInProgress();
}
