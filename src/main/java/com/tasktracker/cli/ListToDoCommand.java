package com.tasktracker.cli;

import com.tasktracker.services.ITaskService;

public class ListToDoCommand extends Command{
    
    private final ITaskService _taskService;
    private final TaskPrinter taskPrinter = new TaskPrinter();
    
    public ListToDoCommand(ITaskService taskService){
        this._taskService = taskService;
    }

    public void execute(){

        var tasks = _taskService.ListTodo();

        if (tasks == null) return;

        taskPrinter.print(tasks);

    }

}
