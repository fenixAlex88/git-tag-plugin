package ru.clevertec.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import ru.clevertec.commandExecutor.CommandExecutor
import ru.clevertec.commandExecutor.CommandExecutorImpl

class CheckGitInstalledTask extends DefaultTask {
    public static final String NAME = 'checkGitInstalled'
    static final String CHECK_GIT_INSTALLED = 'git --version'

    @Internal
    final CommandExecutor executor = new CommandExecutorImpl()

    @TaskAction
    void checkGitInstalled() {
            executor.execute(CHECK_GIT_INSTALLED, 'Git not installed', false)
            println 'Git installed'
    }
}
