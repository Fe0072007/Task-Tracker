package com.tasktracker.cli;

public class Command {

    public void execute(){};

    public void InvalidCommand(String message){
        System.out.printf("""
            
            Erro: %s

            Use --help para checar os comandos.
                
        """, message);
    }
    
}
