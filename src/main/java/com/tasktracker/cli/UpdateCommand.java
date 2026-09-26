package com.tasktracker.cli;

import com.tasktracker.services.ITaskService;

public class UpdateCommand{

    private final ITaskService _taskService;

    public UpdateCommand(ITaskService taskService){
        this._taskService = taskService;
    }
    
    public void execute(long id, String description){

        var task = _taskService.Update(id, description);

        System.out.println("Tarefa "+ task.getId() +" atualizada com sucesso");

    }
    
    public void executeProgress(long id){
        var task = _taskService.MarkProgress(id);

        System.out.println("Tarefa "+ task.getId()+" marcada como em progresso");

    }

    public void executeDone(long id){

        var task = _taskService.MarkDone(id);

        System.out.println("Tarefa "+task.getId()+" marcada como feita");

    }
}
