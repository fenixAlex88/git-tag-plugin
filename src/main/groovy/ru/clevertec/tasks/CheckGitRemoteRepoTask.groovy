package ru.clevertec.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import ru.clevertec.commandExecutor.CommandExecutor
import ru.clevertec.commandExecutor.CommandExecutorImpl

class CheckGitRemoteRepoTask extends DefaultTask {
    public static final String NAME = 'checkGitRemoteRepo'
    static final String CHECK_GIT_REMOTE_REPO = 'git remote'

    @Internal
    final CommandExecutor executor = new CommandExecutorImpl()

    @TaskAction
    void checkGitRemoteRepo() {
        executor.execute(CHECK_GIT_REMOTE_REPO, 'This is not a git remote repository.', true)
        println 'Git remote repository exists'
    }
}
