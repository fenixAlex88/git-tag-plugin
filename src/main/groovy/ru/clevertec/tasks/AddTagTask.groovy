package ru.clevertec.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import ru.clevertec.commandExecutor.CommandExecutor
import ru.clevertec.commandExecutor.CommandExecutorImpl
import ru.clevertec.extension.GitTagExtension

class AddTagTask extends DefaultTask {
    public static final String NAME = 'addTag'
    static final String SET_TAG = 'git tag '
    @Internal
    final CommandExecutor executor = new CommandExecutorImpl()

    @TaskAction
    void checkGitInstalled() {
        def gitTag = project.extensions.findByType(GitTagExtension)
        def newTag = gitTag.getNewTag()
        def currentTag = gitTag.getCurrentTag()
        def hasUncommittedChanges = gitTag.getHasUncommittedChanges()
        if (hasUncommittedChanges) {
            println "Build version with uncommitted changes: ${newTag}.uncommitted"
            return
        }
        if (currentTag) {
            println "Current state already has a tag: ${currentTag}. No new tag will be created."
            return
        }
        if (!newTag) {
            throw new GradleException('newVersion property is not set. Ensure determineVersion task is executed.')
        }
        executor.execute(SET_TAG + newTag, "Error adding tag", false)
        println "Tag \"${newTag}\" added"
    }
}
