package ru.clevertec.taskExecutorImpl

import org.gradle.api.GradleException
import org.gradle.api.Project
import ru.clevertec.TaskExecutor

class GitCommandExecutor implements TaskExecutor{
    private final String command
    private final String successMessage
    private final String errorMessage

    GitCommandExecutor(String command, String successMessage, String errorMessage) {
        this.command = command
        this.successMessage = successMessage
        this.errorMessage = errorMessage
    }

    @Override
    void execute(Project project) {
        try {
            def result = command.execute().text.trim()
            println "${successMessage}"
        } catch (Exception ignored) {
            throw new GradleException(errorMessage)
        }
    }
}
