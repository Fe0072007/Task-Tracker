package com.tasktracker.cli;

import com.tasktracker.services.ITaskService;

public class AddCommand extends Command{
    
    private final ITaskService _taskService;
    private final String description;

    public AddCommand(ITaskService taskService, String description){
        this._taskService = taskService;
        this.description = description;
    }

    public void execute(){

        var task = _taskService.Add(description);

        if(task == null) return;

        System.out.println("Task adicionada com sucesso (ID: "+ task.getId() +")");
    }

}
