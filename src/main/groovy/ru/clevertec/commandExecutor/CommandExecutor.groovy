package ru.clevertec.commandExecutor

interface CommandExecutor {
    String execute(String command, String errorMessage, boolean required)

}