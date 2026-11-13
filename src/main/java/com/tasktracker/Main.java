package com.tasktracker;

import com.tasktracker.cli.CommandParser;
import com.tasktracker.cli.Command;




public class Main {
    public static void main(String[] args) {


        CommandParser parser = new CommandParser();

        Command command = parser.parse(args);

        if(command != null){
            command.execute();
        } else {
            System.out.println("");
        }
    }
}
