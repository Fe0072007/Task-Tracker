package com.tasktracker.cli;

public class HelpCommand extends Command{
    
    @Override
    public void execute(){

        System.out.println("""
                
                Task CLI - Task Tracker
                
                Usage:
                  task-cli <command> [arguments]
                
                Commands:
                  add <description>               Add a new task
                  list-todo                       List pending tasks
                  list-done                       List done tasks
                  list-in-progress                List in progress tasks
                  list-all                        List all tasks
                  update <id> <description>       Update a task
                  mark-progress <id>              Mark task as in progress
                  mark-done <id>                  Mark task as done
                  delete <id>                     Delete a task
                
                Options:
                  --help                          Show this help
                  --version                       Show version
                
                """);

    }

}
