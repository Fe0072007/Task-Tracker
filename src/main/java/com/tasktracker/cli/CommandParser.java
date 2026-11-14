package com.tasktracker.cli;

import com.tasktracker.services.ITaskService;

public class CommandParser{

    private final ITaskService _taskService;

    public CommandParser(ITaskService taskService){
        this._taskService = taskService;
    }

    public void InvalidCommand(String message){
        System.out.printf("""
            
            Erro: %s

            Use --help para checar os comandos.
                
        """, message);
    }
    
    public Command parse(String[] args){

        long id;

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

                try{
                    id = Long.parseLong(args[1]);
                } catch(NumberFormatException ex){
                    InvalidCommand("O ID deve ser um número");
                    return null;
                }

                if(id <=0){
                    InvalidCommand("O ID deve ser maior que 0");
                    return null;
                }

                return new UpdateCommand(_taskService, id, args[2]);

            case "delete":
                // delete <id>

                if (args.length < 2){
                    InvalidCommand("Insira o ID da tarefa");
                    return null;
                }

                try{
                    id = Long.parseLong(args[1]);
                } catch(NumberFormatException ex){
                    InvalidCommand("O ID deve ser um número");
                    return null;
                }

                if(id <=0){
                    InvalidCommand("O ID deve ser maior que 0");
                    return null;
                }
                
                return new DeleteCommand(_taskService, id);

            case "mark-progress":
                // mark-progress <id>
                if (args.length < 2){
                    InvalidCommand("Insira o ID da tarefa");
                    return null;
                }

                try{
                    id = Long.parseLong(args[1]);
                } catch(NumberFormatException ex){
                    InvalidCommand("O ID deve ser um número");
                    return null;
                }

                if(id <=0){
                    InvalidCommand("O ID deve ser maior que 0");
                    return null;
                }

                return new MarkProgressCommand(_taskService, id);

            case "mark-done": 
                // mark-done <id>

                if (args.length < 2){
                    InvalidCommand("Insira o ID da tarefa");
                    return null;
                }

                try{
                    id = Long.parseLong(args[1]);
                } catch(NumberFormatException ex){
                    InvalidCommand("O ID deve ser um número");
                    return null;
                }

                if(id <=0){
                    InvalidCommand("O ID deve ser maior que 0");
                    return null;
                }

                return new MarkDoneCommand(_taskService, id);

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
