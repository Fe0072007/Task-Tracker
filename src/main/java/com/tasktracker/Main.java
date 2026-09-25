package com.tasktracker;

import com.tasktracker.services.ITaskService;
import com.tasktracker.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;



public class Main {
    public static void main(String[] args) {

        ITaskService _taskService = new TaskService();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        if(args.length == 0){
            System.out.println("Use: task-cli <command>\nDigite -help para exibir os comandos");
            return;

        }

        switch(args[0]){
            case "-help":
                String commands[] = {"add <description>", "update <id> <description>", "delete  <id>", "mark-progress <id>", "mark-done <id>", "list-all", "list-done", "list-todo", "list-in-progress"};

                for(String command : commands){
                    System.out.println(command);
                }

                break;
            case "add":
                // add <description>
                // # Output: Task added successfully (ID: 1)
                var addedTask = _taskService.Add(args[1]);

                System.out.println("Tarefa adicionada com sucesso");
                try{
                    System.out.println(
                        objectMapper
                            .writerWithDefaultPrettyPrinter()
                            .writeValueAsString(addedTask)
                    );
                } catch (IOException e){
                    e.printStackTrace();
                }

                break;
            case "update":
                // update <id> <description>

                var updatedTask = _taskService.Update(Long.parseLong(args[1]), args[2]);

                System.out.println("Tarefa atualizada com sucesso");
                try{
                    System.out.println(
                        objectMapper
                            .writerWithDefaultPrettyPrinter()
                            .writeValueAsString(updatedTask)
                    );
                } catch (IOException e){
                    e.printStackTrace();
                }

                break;
            case "delete":
                // delete <id>

                if(_taskService.Delete(Long.parseLong(args[1]))){
                    System.out.println("Tarefa deletada com sucesso!");
                }

                break;
            case "mark-progress":
                // mark-progress <id>

                var inProgressTask = _taskService.MarkProgress(Long.parseLong(args[1]));

                System.out.println("Tarefa atualizada com sucesso");
                try{
                    System.out.println(
                        objectMapper
                            .writerWithDefaultPrettyPrinter()
                            .writeValueAsString(inProgressTask)
                    );
                } catch (IOException e){
                    e.printStackTrace();
                }

                break;
            case "mark-done":
                // mark-done <id>

                var doneTask = _taskService.MarkDone(Long.parseLong(args[1]));

                System.out.println("Tarefa atualizada com sucesso");
                try{
                    System.out.println(
                        objectMapper
                            .writerWithDefaultPrettyPrinter()
                            .writeValueAsString(doneTask)
                    );
                } catch (IOException e){
                    e.printStackTrace();
                }

                break;
            case "list-all":
                // list-all

                var tasks = _taskService.List();

                try{
                    System.out.println(
                        objectMapper
                            .writerWithDefaultPrettyPrinter()
                            .writeValueAsString(tasks)
                    );
                } catch (IOException e){
                    e.printStackTrace();
                }


                break;
            case "list-done":
                // list-done

                var doneTasks = _taskService.ListDone();

                try{
                    System.out.println(
                        objectMapper
                            .writerWithDefaultPrettyPrinter()
                            .writeValueAsString(doneTasks)
                    );
                } catch (IOException e){
                    e.printStackTrace();
                }

                break;
            case "list-todo":
                // list-todo

                var toDoTasks = _taskService.ListTodo();

                try{
                    System.out.println(
                        objectMapper
                            .writerWithDefaultPrettyPrinter()
                            .writeValueAsString(toDoTasks)
                    );
                } catch (IOException e){
                    e.printStackTrace();
                }

                break;
            case "list-in-progress":
                // list-in-progress

                var inProgressTasks = _taskService.ListInProgress();

                try{
                    System.out.println(
                        objectMapper
                            .writerWithDefaultPrettyPrinter()
                            .writeValueAsString(inProgressTasks)
                    );
                } catch (IOException e){
                    e.printStackTrace();
                }

                break;
            default:
                System.out.println("Use: task-cli <command>\nDigite -help para exibir os comandos");
        }
    }
}
