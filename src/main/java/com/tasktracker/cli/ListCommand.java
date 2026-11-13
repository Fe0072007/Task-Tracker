package com.tasktracker.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tasktracker.services.ITaskService;

public class ListCommand extends Command{

    private final ObjectMapper objectMapper;
    private final ITaskService _taskService;

    public ListCommand(ITaskService taskService){
        this._taskService = taskService;

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void execute(){
        var tasks = _taskService.List();

        if (tasks == null) return;

        System.out.printf("%-5s %-30s %-15s %-15s %-15s%n",
        "ID","Description", "Status", "Criação", "Atualização");
        for (var task : tasks) {
            System.out.printf("%-5d %-30s %-15s %-15s %-15s%n",
            task.getId(), task.getDescription(), task.getStatus(), task.getCreatedAt(), task.getUpdatedAt());
        }
    }
    
}
