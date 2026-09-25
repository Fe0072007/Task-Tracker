package com.tasktracker.repository;

import com.tasktracker.model.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tasktracker.model.Status;

public class TaskRepository implements ITaskRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public TaskRepository() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override 
    public Path createJSON(){
        try{
            String appData = System.getenv("APPDATA");

            Path directory = Paths.get(appData, "TaskCLI");

            Files.createDirectories(directory);

            Path filePath = directory.resolve("tasks.json");
            

            if(!Files.exists(filePath)){
                Files.write(
                    filePath,
                    "[]".getBytes(StandardCharsets.UTF_8)
                );
            }

            return filePath;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
    }

    @Override
    public Task add(String description) {
        

        Path filePath = createJSON();

        try{

            List<Task> tasks = list();

            long nextId = tasks.stream()
                .mapToLong(Task::getId)
                .max()
                .orElse(0) + 1;

            Task newTask = new Task(nextId, description);

            tasks.add(newTask);

            objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(filePath.toFile(), tasks);
            
            return newTask;

        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Task update(long id, Status status){
        Path filePath = createJSON();
        Task task = null;

        try{

            List<Task> tasks = list();

            if(tasks.isEmpty()){
                System.out.println("Não existe nenhuma tarefa para ser modificada");
                return null;
            }

            boolean found = false;

            for (int i = 0; i < tasks.size(); i++){
                if(tasks.get(i).getId() == id){
                    found = true;
                    task = tasks.get(i);
                    task.setStatus(status);
                    task.setUpdatedAt(LocalDate.now());
                    tasks.set(i, task);

                    break;
                }
            }

            if(!found){ System.out.println("Tarefa não encontrada"); return null; }

            objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(filePath.toFile(), tasks);
            
            return task;


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Task update(long id, String description){
        Path filePath = createJSON();
        Task task = null;

        try{

            List<Task> tasks = list();

            if(tasks.isEmpty()){
                System.out.println("Não existe nenhuma tarefa para ser modificada");
                return null;
            }

            boolean found = false;

            for (int i = 0; i < tasks.size(); i++){
                if(tasks.get(i).getId() == id){
                    found = true;
                    task = tasks.get(i);
                    task.setDescription(description);
                    task.setUpdatedAt(LocalDate.now());
                    tasks.set(i, task);
                }
            }

            if(!found){ System.out.println("Tarefa não encontrada"); return null; }

            objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(filePath.toFile(), tasks);
            
            return task;


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Task> delete(long id) {
        Path filePath = createJSON();

        try{

            List<Task> tasks = list();

            if(tasks.isEmpty()){
                System.out.println("Não existe nenhuma tarefa para ser deletada");
                return null;
            }

            boolean found = false;

            for (int i = 0; i < tasks.size(); i++){
                if(tasks.get(i).getId() == id){
                    tasks.remove(i);
                    found = true;
                    break;
                }
            }

            if(!found){ System.out.println("Tarefa não encontrada"); return null; }

            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(filePath.toFile(), tasks);
            
            return tasks; 

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public List<Task> list() {
        try{
            Path filePath = createJSON();

            List<Task> tasks = objectMapper.readValue(
                filePath.toFile(),
                new TypeReference<List<Task>>() {}
            );

            return tasks;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Task> list(Status status){
        try{
            Path filePath = createJSON();

            List<Task> tasks = objectMapper.readValue(
                filePath.toFile(),
                new TypeReference<List<Task>>() {}
            );

            List<Task> listStatus = new ArrayList<>();

            for (Task task : tasks) {
                if(task.getStatus() == status){
                    listStatus.add(task);
                }
            }

            return listStatus;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
