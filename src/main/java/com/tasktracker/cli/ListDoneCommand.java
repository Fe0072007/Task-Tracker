package com.tasktracker.cli;

import com.tasktracker.services.ITaskService;

public class ListDoneCommand extends Command{
    private final ITaskService _taskService;
    private final TaskPrinter taskPrinter = new TaskPrinter();
    
    public ListDoneCommand(ITaskService taskService){
        this._taskService = taskService;
    }

    public void execute(){

        var tasks = _taskService.ListDone();

        if (tasks == null) return;

        taskPrinter.print(tasks);

    }

}
