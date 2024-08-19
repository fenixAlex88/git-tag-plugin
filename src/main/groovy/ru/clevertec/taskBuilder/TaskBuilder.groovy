package ru.clevertec.taskBuilder

import ru.clevertec.TaskNames
import ru.clevertec.taskExecutor.TaskExecutor

interface TaskBuilder {
    TaskBuilder withGroup(String group)
    TaskBuilder withExecutor(TaskExecutor executor)
    TaskBuilder dependsOn(TaskNames... tasks)
    void build()
}