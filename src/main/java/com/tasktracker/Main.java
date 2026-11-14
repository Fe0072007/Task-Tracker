package com.tasktracker;

import com.tasktracker.cli.CommandParser;
import com.tasktracker.repository.ITaskRepository;
import com.tasktracker.repository.TaskRepository;
import com.tasktracker.model.Exceptions.PersistenceException;
import com.tasktracker.services.ITaskService;
import com.tasktracker.services.TaskService;
import com.tasktracker.cli.Command;




public class Main {
    public static void main(String[] args) {


        ITaskRepository repository = new TaskRepository();
        ITaskService service = new TaskService(repository);
        CommandParser parser = new CommandParser(service);

        Command command = parser.parse(args);

        try {
            if (command != null) {
                command.execute();
            }
        } catch (PersistenceException ex) {
            System.err.println("Erro: " + ex.getMessage());
        }
    }
}
