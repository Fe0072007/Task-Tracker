package com.tasktracker;

import com.tasktracker.cli.AddCommand;
import com.tasktracker.cli.CommandParser;
import com.tasktracker.cli.DeleteCommand;
import com.tasktracker.cli.ListCommand;
import com.tasktracker.cli.UpdateCommand;
import com.tasktracker.services.ITaskService;
import com.tasktracker.services.TaskService;




public class Main {
    public static void main(String[] args) {

        ITaskService _taskService = new TaskService();
        // ObjectMapper objectMapper = new ObjectMapper();
        // objectMapper.registerModule(new JavaTimeModule());

        CommandParser parser = new CommandParser();

        String command = parser.parse(args);

        switch(command){

            case "-help":
                String commands[] = {"add <description>", "update <id> <description>", "delete  <id>", "mark-progress <id>", "mark-done <id>", "list-all", "list-done", "list-todo", "list-in-progress"};

                for(String item : commands){
                    System.out.println(item);
                }

                break;
            case "add":
                // add <description>
                // # Output: Task added successfully (ID: 1)
                AddCommand addCommand = new AddCommand(_taskService);

                addCommand.execute(args[1]);

                break;
            case "update":
                // update <id> <description>

                UpdateCommand updateCommand = new UpdateCommand(_taskService);

                updateCommand.execute(Long.parseLong(args[1]), args[2]);

                break;
            case "delete":
                // delete <id>

                DeleteCommand deleteCommand = new DeleteCommand(_taskService);

                deleteCommand.execute(Long.parseLong(args[1]));

                break;
            case "mark-progress":
                // mark-progress <id>

                UpdateCommand progressCommand = new UpdateCommand(_taskService);

                progressCommand.executeProgress(Long.parseLong(args[1]));

                break;
            case "mark-done": 
                // mark-done <id>

                UpdateCommand doneTask = new UpdateCommand(_taskService);

                doneTask.executeDone(Long.parseLong(args[1]));

                break;
            case "list-all":
                // list-all

                ListCommand listAllCommand = new ListCommand(_taskService);

                listAllCommand.execute();

                break;
            case "list-done":
                // list-done

                ListCommand listDoneCommand = new ListCommand(_taskService);

                listDoneCommand.executeDone();

                break;
            case "list-todo":
                // list-todo

                ListCommand listToDoCommand = new ListCommand(_taskService);

                listToDoCommand.executeToDo();

                break;
            case "list-in-progress":
                // list-in-progress

                ListCommand listProgressCommand = new ListCommand(_taskService);

                listProgressCommand.executeInProgress();

                break;
            default:

                System.out.println("Use: task-cli <command>\nDigite -help para exibir os comandos");
        
        }
    }
}
