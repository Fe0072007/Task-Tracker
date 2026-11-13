package com.tasktracker.cli;

import com.tasktracker.services.ITaskService;

public class DeleteCommand extends Command{
    
    private final ITaskService _taskService;
    private final long id;
    
    public DeleteCommand(ITaskService taskService, long id){
        this._taskService = taskService;
        this.id = id;
    }

    public void execute(){

        if(!_taskService.Delete(id)) return;

        System.out.println("Tarefa excluída com sucesso");

    }

}
