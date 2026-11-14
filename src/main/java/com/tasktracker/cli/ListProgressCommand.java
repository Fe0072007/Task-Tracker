package com.tasktracker.cli;

import com.tasktracker.services.ITaskService;

public class ListProgressCommand extends Command{
    private final ITaskService _taskService;
    private final TaskPrinter taskPrinter = new TaskPrinter();
    
    public ListProgressCommand(ITaskService taskService){
        this._taskService = taskService;
    }

    public void execute(){

        var tasks = _taskService.ListInProgress();

        if (tasks == null) return;

        taskPrinter.print(tasks);

    }

}
