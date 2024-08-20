package ru.clevertec.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import ru.clevertec.commandExecutor.CommandExecutor
import ru.clevertec.commandExecutor.CommandExecutorImpl

class CheckGitRepoTask extends DefaultTask {
    public static final String NAME = 'checkGitRepo'
    static final String CHECK_GIT_REPO = 'git status'

    @Internal
    final CommandExecutor executor = new CommandExecutorImpl()

    @TaskAction
    void checkGitRepo() {
            executor.execute(CHECK_GIT_REPO, 'This is not a git repository.', true)
            println 'Git repository exists'
    }
}
