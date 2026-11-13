package com.tasktracker.cli;

import com.tasktracker.services.ITaskService;

public class MarkDoneCommand extends Command{

    private final ITaskService _taskService;
    private final long id;

    public MarkDoneCommand(ITaskService taskService, long id){
        this._taskService = taskService;
        this.id = id;
    }
    
    public void execute(){

        var task = _taskService.MarkDone(id);

        System.out.println("Tarefa "+task.getId()+" marcada como feita");

    }

}
