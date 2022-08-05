package com.bots.RacoonServer.Logging;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.awt.*;

public class Log {
    private final String caller;
    private final String message;
    private final Color color;

    public Log(String message, Color color) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        this.caller = context.getBean(CallerAcquirement.class).getClassName();
        this.message = message;
        this.color = color;
    }

    public Log(String caller, String message, Color color) {
        this.caller = caller;
        this.message = message;
        this.color = color;
    }

    public String getCaller() {
        return caller;
    }

    public String getMessage() {
        return message;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return caller + ": " + message;
    }
}
