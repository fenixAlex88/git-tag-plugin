package ru.clevertec.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import ru.clevertec.commandExecutor.CommandExecutor
import ru.clevertec.commandExecutor.CommandExecutorImpl
import ru.clevertec.extension.GitTagExtension

class GetCurrentTagTask extends DefaultTask {
    public static final String NAME = 'getCurrentTag'
    static final String GET_CURRENT_TAG = 'git describe --tags --exact-match'

    @Internal
    final CommandExecutor executor = new CommandExecutorImpl()

    @TaskAction
    void getCurrentTag() {
        def gitTag = project.extensions.findByType(GitTagExtension)
        def currentTag = executor.execute(GET_CURRENT_TAG, 'Error retrieving current tag', false)
        println currentTag ? "Current tag: ${currentTag}" : "No current tag"
        gitTag.setCurrentTag(currentTag)
    }
}
