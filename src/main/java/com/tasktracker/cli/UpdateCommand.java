package com.tasktracker.cli;

import com.tasktracker.services.ITaskService;

public class UpdateCommand extends Command{

    private final ITaskService _taskService;
    private final long id;
    private final String description;

    public UpdateCommand(ITaskService taskService, long id, String description){
        this._taskService = taskService;
        this.id = id;
        this.description = description;
    }
    
    public void execute(){

        var task = _taskService.Update(id, description);

        if (task == null) return;

        System.out.println("Tarefa "+ task.getId() +" atualizada com sucesso");

    }

}
