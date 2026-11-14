package com.tasktracker.cli;

public class VersionCommand extends Command{
    
    public static final String version = "1.1.1";

    @Override
    public void execute(){

        
        System.out.println("Task CLI v" + version);

    }

}
