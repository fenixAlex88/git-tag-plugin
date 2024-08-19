package ru.clevertec.taskBuilder

import org.gradle.api.Project
import org.gradle.api.Task
import ru.clevertec.TaskExecutor
import ru.clevertec.TaskName

class ExecuteTaskBuilder {
    private final Project project
    private final TaskName taskName
    private String group
    private TaskExecutor executor
    private List<String> dependencies = []

    ExecuteTaskBuilder(Project project, TaskName taskName) {
        this.project = project
        this.taskName = taskName
    }

    ExecuteTaskBuilder withGroup(String group) {
        this.group = group
        return this
    }

    ExecuteTaskBuilder withExecutor(TaskExecutor executor) {
        this.executor = executor
        return this
    }

    ExecuteTaskBuilder dependsOn(TaskName... tasks) {
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