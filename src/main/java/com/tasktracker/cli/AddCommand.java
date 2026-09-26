package com.tasktracker.cli;

import com.tasktracker.services.ITaskService;

public class AddCommand {
    
    private final ITaskService _taskService;

    public AddCommand(ITaskService taskService){
        this._taskService = taskService;
    }

    public void execute(String description){

        var task = _taskService.Add(description);

        System.out.println("Task adicionada com sucesso (ID: "+ task.getId() +")");
    }

}
