package ru.clevertec.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import ru.clevertec.commandExecutor.CommandExecutor
import ru.clevertec.commandExecutor.CommandExecutorImpl
import ru.clevertec.extension.GitTagExtension

class GetCurrentBranchTask extends DefaultTask {
    public static final String NAME = 'getCurrentBranch'
    static final String GET_CURRENT_BRANCH = 'git branch --show-current'

    @Internal
    final CommandExecutor executor = new CommandExecutorImpl()

    @TaskAction
    void getCurrentBranch() {
        def gitTag = project.extensions.findByType(GitTagExtension)
        String currentBranch = executor.execute(GET_CURRENT_BRANCH, 'Error retrieving current branch', true)
        println "Current branch: ${currentBranch}"
        gitTag.setCurrentBranch(Optional.of(currentBranch))
    }
}