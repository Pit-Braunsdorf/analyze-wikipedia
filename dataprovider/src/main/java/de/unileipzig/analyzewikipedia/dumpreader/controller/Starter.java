package de.unileipzig.analyzewikipedia.dumpreader.controller;

/**
 * @author Danilo
 * 
 * class starts and inits the java application
 */
public class Starter {

    /**
     * MAIN: start the java file
     * 
     * @param args as string array
     */
    public static void main(String[] args) {
        
        try {
            // load the threads and start work
            ThreadController.initApplication(args);
        } catch (Exception ex) {}
                     
    }
    
}
