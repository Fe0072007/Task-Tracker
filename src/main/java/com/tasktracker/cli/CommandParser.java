package com.tasktracker.cli;

import com.tasktracker.services.ITaskService;
import com.tasktracker.services.TaskService;

public class CommandParser extends Command{

    private final ITaskService _taskService = new TaskService();
    
    public Command parse(String[] args){

        if(args.length == 0) return new HelpCommand();

        switch(args[0]){
            case "--help":
                
                return new HelpCommand();

            case "--version":
                
                return new VersionCommand();

            case "add":
                // add <description>
                // # Output: Task added successfully (ID: 1)
                if(args.length < 2){
                    InvalidCommand("Insira a descrição da tarefa");
                    return null;
                }

                return new AddCommand(_taskService, args[1]);

            case "update":
                // update <id> <description>
                if (args.length < 2){
                    InvalidCommand("Insira o ID da tarefa");
                    return null;
                }
                else if (args.length < 3) {
                    InvalidCommand("Insira a descrição da tarefa");
                    return null;
                }

                return new UpdateCommand(_taskService, Long.parseLong(args[1]), args[2]);

            case "delete":
                // delete <id>
                if (args.length < 2){
                    InvalidCommand("Insira o ID da tarefa");
                    return null;
                }
                
                return new DeleteCommand(_taskService, Long.parseLong(args[1]));

            case "mark-progress":
                // mark-progress <id>
                if (args.length < 2){
                    InvalidCommand("Insira o ID da tarefa");
                    return null;
                }

                return new MarkProgressCommand(_taskService, Long.parseLong(args[1]));

            case "mark-done": 
                // mark-done <id>

                if (args.length < 2){
                    InvalidCommand("Insira o ID da tarefa");
                    return null;
                }

                return new MarkDoneCommand(_taskService, Long.parseLong(args[1]));

            case "list-all":
                // list-all

                return new ListCommand(_taskService);

            case "list-done":
                // list-done

                return new ListDoneCommand(_taskService);

            case "list-todo":
                // list-todo

                return new ListToDoCommand(_taskService);

            case "list-in-progress":
                // list-in-progress

                return new ListProgressCommand(_taskService);

            default:

                System.out.println(
                    "Error: Comando desconhecido '" + args[0] + "'."
                );
                System.out.println(
                    "Use 'task-cli --help' para ver os comandos disponíveis."
                );
                return null;
        
        }
    }

}
