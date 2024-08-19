package ru.clevertec.taskExecutorImpl

import org.gradle.api.Project
import ru.clevertec.TaskExecutor

class CustomTaskExecutor implements TaskExecutor{
    private final Closure action

    CustomTaskExecutor(Closure action) {
        this.action = action
    }

    @Override
    void execute(Project project) {
        action.call(project)
    }
}
