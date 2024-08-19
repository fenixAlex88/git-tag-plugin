package ru.clevertec

import org.gradle.api.Project

interface TaskExecutor {
    void execute(Project project)
}