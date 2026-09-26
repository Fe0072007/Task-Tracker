package com.tasktracker.cli;

public class CommandParser {
    
    public String parse(String[] args){

        if(args.length == 0) return "-help";

        return args[0].toLowerCase();

    }

}
