package ru.clevertec.taskExecutor

import org.gradle.api.Project

interface TaskExecutor {
    void execute(Project project)
}