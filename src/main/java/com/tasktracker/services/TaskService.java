package com.tasktracker.services;

import com.tasktracker.model.Task;
import com.tasktracker.model.Exceptions.EmptyException;
import com.tasktracker.model.Exceptions.NotFoundException;
import com.tasktracker.repository.ITaskRepository;
import com.tasktracker.model.Status;

import java.util.List;

public class TaskService implements ITaskService{

    private final ITaskRepository _taskRepository;

    public TaskService(ITaskRepository taskRepository){
        this._taskRepository = taskRepository;
    }

    @Override
    public Task Add(String description) {

        if (description == null || description.isBlank()){
            System.out.println("A descrição da tarefa não pode estar vazia");
            return null; 
        }

        Task task = _taskRepository.add(description);
        if (task != null){
            return task;
        } else {
            return null;
        }

    }

    @Override
    public Task Update(long id, String description) {
        try {

            if (description == null || description.isBlank()){
                System.out.println("A descrição da tarefa não pode estar vazia");
                return null;
            }

            Task task = _taskRepository.update(id, description);

            if (task != null){
                return task;
            } else {
                return null;
            }

        } catch (EmptyException | NotFoundException ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public boolean Delete(long id) {
        try{
            List<Task> tasks = _taskRepository.delete(id);

            return tasks != null;
        } catch (EmptyException | NotFoundException ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }

    @Override
    public Task MarkProgress(long id) {

        try{
            Task task = _taskRepository.update(id, Status.IN_PROGRESS);

            if (task != null){
                return task;
            } else {
                return null;
            }
        }catch(EmptyException | NotFoundException ex){
            System.out.println(ex.getMessage());
            return null;
        }

    }

    @Override
    public Task MarkDone(long id) {

        try{
            Task task = _taskRepository.update(id, Status.DONE);

            if (task != null){
                return task;
            } else {
                return null;
            }
        } catch(EmptyException | NotFoundException ex){
            System.out.println(ex.getMessage());
            return null;
        }

    }

    @Override
    public List<Task> List() {
        
        List<Task> tasks = _taskRepository.list();

        if(tasks == null) {
            System.out.println("Não foi possível carregar as tarefas");
            return null;
        }

        if (!tasks.isEmpty()){
            return tasks;
        } else {
            System.out.println("Nenhuma tarefa para ser listada");
            return null;
        }

    }

    @Override
    public List<Task> ListDone() {
        
        List<Task> tasks = _taskRepository.list(Status.DONE);

        if(tasks == null) {
            System.out.println("Não foi possível carregar as tarefas");
            return null;
        }

        if (!tasks.isEmpty()){
            return tasks;
        } else {
            System.out.println("Nenhuma tarefa para ser listada");
            return null;
        }

    }

    @Override
    public List<Task> ListTodo() {
        
        List<Task> tasks = _taskRepository.list(Status.TODO);

        if(tasks == null) {
            System.out.println("Não foi possível carregar as tarefas");
            return null;
        }

        if (!tasks.isEmpty()){
            return tasks;
        } else {
            System.out.println("Nenhuma tarefa para ser listada");
            return null;
        }

    }

    @Override
    public List<Task> ListInProgress() {
        
        List<Task> tasks = _taskRepository.list(Status.IN_PROGRESS);

        if(tasks == null) {
            System.out.println("Não foi possível carregar as tarefas");
            return null;
        }

        if (!tasks.isEmpty()){
            return tasks;
        } else {
            System.out.println("Nenhuma tarefa para ser listada");
            return null;
        }

    }
    

    
}
