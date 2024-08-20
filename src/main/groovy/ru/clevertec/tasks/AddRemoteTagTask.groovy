package ru.clevertec.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import ru.clevertec.commandExecutor.CommandExecutor
import ru.clevertec.commandExecutor.CommandExecutorImpl
import ru.clevertec.extension.GitTagExtension

class AddRemoteTagTask extends DefaultTask {
    public static final String NAME = 'addRemoteTag'
    static final String SET_REMOTE_TAG = 'git push origin tag '

    @Internal
    final CommandExecutor executor = new CommandExecutorImpl()

    @TaskAction
    void addRemoteTag() {
        def gitTag = project.extensions.findByType(GitTagExtension)
        def newTag = gitTag.getNewTag().orElseThrow(() -> new GradleException("New tag is not set"))
        executor.execute(SET_REMOTE_TAG + newTag, "Error adding remote tag", false)
        println "Tag \"${newTag}\" added on remote repository"
    }
}
