package ru.clevertec.commandExecutor

import org.gradle.api.GradleException

class CommandExecutorImpl implements CommandExecutor{
    @Override
    String execute(String command, String errorMessage, boolean required = false) {
        try {
            def result = command.execute().text.trim()
            if (required && result.isEmpty())
                throw new GradleException()
            return result
        } catch (Exception ignored) {
            throw new GradleException(errorMessage)
        }
    }
}
