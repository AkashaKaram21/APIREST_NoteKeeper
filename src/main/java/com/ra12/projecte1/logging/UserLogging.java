package com.ra12.projecte1.logging;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class UserLogging {
    
    static final String LOGS_PATH = "src/main/resources/logs/";

    static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm.ss"); 

    // Metode que troba el nom de la clase i el metode
    private String[] getClassAndName() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        StackTraceElement stackTraceElement = stackTraceElements[3]; // 3 perque al Stack tenim 0 getStackTrace, 1 getClassAndName, 2 logInfo/logError, fent 3 la posició d'on arriba el log 

        return new String[] {stackTraceElement.getClassName().substring(stackTraceElement.getClassName().lastIndexOf(".") + 1), stackTraceElement.getMethodName()};
    }

    // Metode que puja un log de informació i de quina classe i metode arriba
    public void logInfo(String infoMessage) {
        String[] classAndName = getClassAndName();

        String className = classAndName[0];
        String methodName = classAndName[1];  
        
        String timestamp = LocalDateTime.now().format(dateTimeFormatter);

        String logEntry = String.format("[INFO] %s - Class: %s - Method: %s - Message: %s", timestamp, className, methodName, infoMessage);

        writeToFile(logEntry);

        System.out.println(logEntry);
    };

    // Metode que puja un log d'error, de quina classe i metode arriba i si hi ha alguna excepció
    public void logError(String errorMessage, Exception exception) {
        String[] classAndName = getClassAndName();

        String className = classAndName[0];
        String methodName = classAndName[1];
        
        String timestamp = LocalDateTime.now().format(dateTimeFormatter);

        String logEntry = String.format("[ERROR] %s - Class: %s - Method: %s - Message %s", timestamp, className, methodName, errorMessage);

        if (exception != null) {
            logEntry = logEntry + " - Exception: " + exception.getMessage();
        }

        writeToFile(logEntry);

        System.out.println(logEntry);
    };

    // Metode que escriu al arxiu
    public void writeToFile(String message) {
        String LOG_FILE = LOGS_PATH + String.format("aplicacio-%s-%s-%s.log", LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), LocalDateTime.now().getDayOfMonth());

        Path logPath = Paths.get(LOG_FILE);

        try {
            Files.createDirectories(logPath.getParent());

            try (BufferedWriter bufferedWriter = Files.newBufferedWriter(logPath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                bufferedWriter.write(message);
                bufferedWriter.newLine();
            }

        } catch(IOException ioException) {
            System.err.println("ERROR escrivint al fitxer de log: " + ioException.getMessage());
        }
    }
}