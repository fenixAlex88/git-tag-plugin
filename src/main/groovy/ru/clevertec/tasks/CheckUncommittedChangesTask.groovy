package ru.clevertec.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import ru.clevertec.commandExecutor.CommandExecutor
import ru.clevertec.commandExecutor.CommandExecutorImpl
import ru.clevertec.extension.GitTagExtension

class CheckUncommittedChangesTask extends DefaultTask {
    public static final String NAME = 'checkUncommittedChanges'
    static final String CHECK_UNCOMMITTED_CHANGES = 'git status --porcelain'

    @Internal
    final CommandExecutor executor = new CommandExecutorImpl()

    @TaskAction
    void checkUncommittedChanges() {
        def gitTag = project.extensions.findByType(GitTagExtension)
        def uncommittedChanges = executor.execute(CHECK_UNCOMMITTED_CHANGES, 'Error checking uncommitted changes', false)
        def hasUncommittedChanges = !uncommittedChanges.isEmpty()
        gitTag.setHasUncommittedChanges(hasUncommittedChanges)
        if (hasUncommittedChanges) {
            throw new GradleException("Uncommitted changes: ${gitTag.getLastTag()}.uncommitted")
        }
        println "No uncommitted changes"
    }
}
