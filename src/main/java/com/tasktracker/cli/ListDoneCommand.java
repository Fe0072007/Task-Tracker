package com.tasktracker.cli;

import com.tasktracker.services.ITaskService;

public class ListDoneCommand extends Command{
    private final ITaskService _taskService;
    
    public ListDoneCommand(ITaskService taskService){
        this._taskService = taskService;
    }

    public void execute(){

        var tasks = _taskService.ListDone();

        if (tasks == null) return;

        System.out.printf("%-5s %-30s %-15s %-15s%n",
        "ID","Description", "Criação", "Atualização");
        for (var task : tasks) {
            System.out.printf("%-5d %-30s %-15s %-15s%n",
            task.getId(), task.getDescription(), task.getCreatedAt(), task.getUpdatedAt());
        }

    }

}
