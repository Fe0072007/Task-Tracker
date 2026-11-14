package com.tasktracker.cli;

import com.tasktracker.model.Task;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskPrinter {
    private static final DateTimeFormatter DATE_TIME_FORMAT =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public void print(List<Task> tasks) {
        System.out.printf("%-5s %-30s %-15s %-19s %-19s%n",
            "ID", "Description", "Status", "Criação", "Atualização");

        for (Task task : tasks) {
            System.out.printf("%-5d %-30s %-15s %-19s %-19s%n",
                task.getId(), task.getDescription(), task.getStatus(),
                task.getCreatedAt().format(DATE_TIME_FORMAT),
                task.getUpdatedAt().format(DATE_TIME_FORMAT));
        }
    }
}
