package com.tasktracker.cli;

import com.tasktracker.services.ITaskService;

public class DeleteCommand {
    
    private final ITaskService _taskService;
    
    public DeleteCommand(ITaskService taskService){
        this._taskService = taskService;
    }

    public void execute(long id){

        _taskService.Delete(id);

        System.out.println("Tarefa excluída com sucesso");

    }

}
