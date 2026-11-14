package com.tasktracker.repository;

import com.tasktracker.model.Task;
import com.tasktracker.model.Exceptions.EmptyException;
import com.tasktracker.model.Exceptions.NotFoundException;
import com.tasktracker.model.Exceptions.PersistenceException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tasktracker.model.Status;

public class TaskRepository implements ITaskRepository {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Path directory;

    public TaskRepository() {
        this(defaultDirectory());
    }

    public TaskRepository(Path directory) {
        this.directory = java.util.Objects.requireNonNull(directory);
        objectMapper.registerModule(new JavaTimeModule());
    }

    private static Path defaultDirectory() {
        String appData = System.getenv("APPDATA");
        return (appData == null || appData.isBlank())
            ? Paths.get(System.getProperty("user.home"), "TaskCLI")
            : Paths.get(appData, "TaskCLI");
    }

    @Override 
    public Path createJSON(){
        try{
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
            throw new PersistenceException("Não foi possível acessar o arquivo de tarefas.", e);
        }
        
    }

    private void saveTasks(Path filePath, List<Task> tasks) throws IOException{

        Path temp = null;
        IOException failure = null;
    
        try{
            Path directory = filePath.getParent();

            temp = Files.createTempFile(directory, "tasks-", ".tmp");

            objectMapper.
                writerWithDefaultPrettyPrinter().
                writeValue(temp.toFile(), tasks);
            
            Files.move(temp, filePath, StandardCopyOption.ATOMIC_MOVE);

        } catch (IOException ex) {
            failure = ex;
            throw ex;
        } finally {
            if (temp != null) {
                try {
                    Files.deleteIfExists(temp);
                } catch (IOException cleanupEx) {
                    if (failure != null) {
                        failure.addSuppressed(cleanupEx);
                    } else {
                        throw cleanupEx;
                    }
                }
            }
        }

    }
    @Override
    public Task add(String description) {
        

        Path filePath = createJSON();

        try{

            List<Task> tasks = list();

            // nextId pode não corresponder à sequência correta de id Ex.: IDs = [1, 2, 3], caso 3 seja deletado e seja adicionada uma nova task seu id será 3
            long nextId = tasks.stream()
                .mapToLong(Task::getId)
                .max()
                .orElse(0) + 1;

            Task newTask = new Task(nextId, description);

            tasks.add(newTask);

            saveTasks(filePath, tasks);
            
            return newTask;

        }catch (IOException e) {
            throw new PersistenceException("Não foi possível salvar as tarefas.", e);
        }
    }

    @Override
    public Task update(long id, Status status) throws NotFoundException, EmptyException{

        Path filePath = createJSON();

        Task task = null;

        try{

            List<Task> tasks = list();


            if(tasks.isEmpty())
                throw new EmptyException(
                    "Não existe nenhuma tarefa para ser modificada");
            

            boolean found = false;

            for (int i = 0; i < tasks.size(); i++){
                if(tasks.get(i).getId() == id){
                    found = true;
                    task = tasks.get(i);
                    task.setStatus(status);
                    task.setUpdatedAt(OffsetDateTime.now());
                    tasks.set(i, task);

                    break;
                }
            }

            if(!found) 
                throw new NotFoundException(
                            "Error: Nenhuma tarefa com ID: "+ id + " encontrada");  

            saveTasks(filePath, tasks);
                
            
            return task;


        } catch (IOException e) {
            throw new PersistenceException("Não foi possível salvar as tarefas.", e);
        }
    }

    @Override
    public Task update(long id, String description) throws EmptyException, NotFoundException{
        Path filePath = createJSON();
        Task task = null;

        try{

            List<Task> tasks = list();


            if(tasks.isEmpty()){
                throw new EmptyException("Não existe nenhuma tarefa para ser modificada");
            }

            boolean found = false;

            for (int i = 0; i < tasks.size(); i++){
                if(tasks.get(i).getId() == id){
                    found = true;
                    task = tasks.get(i);
                    task.setDescription(description);
                    task.setUpdatedAt(OffsetDateTime.now());
                    tasks.set(i, task);
                }
            }

            if(!found)
                throw new NotFoundException(
                            "Error: Nenhuma tarefa com ID: "+ id + " encontrada"); 
            

            saveTasks(filePath, tasks);
            
            return task;


        } catch (IOException e) {
            throw new PersistenceException("Não foi possível salvar as tarefas.", e);
        }
    }

    @Override
    public List<Task> delete(long id) throws EmptyException, NotFoundException {
        Path filePath = createJSON();

        try{

            List<Task> tasks = list();


            if(tasks.isEmpty())
                throw new EmptyException(
                    "Nenhuma tarefa para ser deletada");
            

            boolean found = false;

            for (int i = 0; i < tasks.size(); i++){
                if(tasks.get(i).getId() == id){
                    tasks.remove(i);
                    found = true;
                    break;
                }
            }

            if(!found)
                throw new NotFoundException(
                        "Error: Nenhuma tarefa com ID: "+ id + " encontrada");
             

            saveTasks(filePath, tasks);
            
            return tasks; 

        } catch (IOException e) {
            throw new PersistenceException("Não foi possível salvar as tarefas.", e);
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
            throw new PersistenceException("Não foi possível ler o arquivo de tarefas.", e);
        }
    }

    @Override
    public List<Task> list(Status status) {
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
            throw new PersistenceException("Não foi possível ler o arquivo de tarefas.", e);
        }
    }

}
