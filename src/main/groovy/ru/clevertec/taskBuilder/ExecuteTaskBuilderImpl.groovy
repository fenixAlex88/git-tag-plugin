package ru.clevertec.taskBuilder

import org.gradle.api.Project
import org.gradle.api.Task
import ru.clevertec.taskExecutor.TaskExecutor
import ru.clevertec.TaskNames

class ExecuteTaskBuilderImpl implements TaskBuilder {
    private final Project project
    private final TaskNames taskName
    private String group
    private TaskExecutor executor
    private List<String> dependencies = []

    ExecuteTaskBuilderImpl(Project project, TaskNames taskName) {
        this.project = project
        this.taskName = taskName
    }

    ExecuteTaskBuilderImpl withGroup(String group) {
        this.group = group
        return this
    }

    ExecuteTaskBuilderImpl withExecutor(TaskExecutor executor) {
        this.executor = executor
        return this
    }

    ExecuteTaskBuilderImpl dependsOn(TaskNames... tasks) {
        this.dependencies.addAll(tasks.collect { it.getName() })
        return this
    }

    void build() {
        project.tasks.register(taskName.getName()) { Task task ->
            if (this.group != null) {
                task.group = this.group
            }
            if (!dependencies.isEmpty()) {
                task.dependsOn(dependencies)
            }
            task.doLast {
                executor.execute(project)
            }
        }
    }
}