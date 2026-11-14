package com.tasktracker.cli;

import com.tasktracker.services.ITaskService;

public class ListCommand extends Command{

    private final ITaskService _taskService;
    private final TaskPrinter taskPrinter = new TaskPrinter();

    public ListCommand(ITaskService taskService){
        this._taskService = taskService;

    }

    @Override
    public void execute(){
        var tasks = _taskService.List();

        if (tasks == null) return;

        taskPrinter.print(tasks);
    }
    
}
