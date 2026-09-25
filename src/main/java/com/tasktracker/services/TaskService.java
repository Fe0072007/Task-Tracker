package com.tasktracker.services;

import com.tasktracker.model.Task;
import com.tasktracker.repository.ITaskRepository;
import com.tasktracker.repository.TaskRepository;
import com.tasktracker.model.Status;

import java.util.List;

public class TaskService implements ITaskService{

    ITaskRepository _taskRepository = new TaskRepository();

    @Override
    public Task Add(String description) {

        Task task = _taskRepository.add(description);
        if (task != null){
            return task;
        } else {
            return null;
        }

    }

    @Override
    public Task Update(long id, String description) {
        Task task = _taskRepository.update(id, description);
        if (task != null){
            return task;
        } else {
            return null;
        }
    }

    @Override
    public boolean Delete(long id) {
        List<Task> tasks = _taskRepository.delete(id);

        if (!tasks.isEmpty()){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Task MarkProgress(long id) {

        Task task = _taskRepository.update(id, Status.IN_PROGRESS);

        if (task != null){
            return task;
        } else {
            return null;
        }

    }

    @Override
    public Task MarkDone(long id) {
        
        Task task = _taskRepository.update(id, Status.DONE);

        if (task != null){
            return task;
        } else {
            return null;
        }

    }

    @Override
    public List<Task> List() {
        
        List<Task> tasks = _taskRepository.list();

        if (!tasks.isEmpty()){
            return tasks;
        } else {
            return null;
        }

    }

    @Override
    public List<Task> ListDone() {
        
        List<Task> tasks = _taskRepository.list(Status.DONE);

        if (!tasks.isEmpty()){
            return tasks;
        } else {
            return null;
        }

    }

    @Override
    public List<Task> ListTodo() {
        
        List<Task> tasks = _taskRepository.list(Status.TODO);

        if (!tasks.isEmpty()){
            return tasks;
        } else {
            return null;
        }

    }

    @Override
    public List<Task> ListInProgress() {
        
        List<Task> tasks = _taskRepository.list(Status.IN_PROGRESS);

        if (!tasks.isEmpty()){
            return tasks;
        } else {
            return null;
        }

    }
    

    
}
