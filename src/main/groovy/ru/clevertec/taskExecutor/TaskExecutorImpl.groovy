package ru.clevertec.taskExecutor

import org.gradle.api.Project

class TaskExecutorImpl implements TaskExecutor{
    private final Closure action

    TaskExecutorImpl(Closure action) {
        this.action = action
    }

    @Override
    void execute(Project project) {
        action.call(project)
    }
}
