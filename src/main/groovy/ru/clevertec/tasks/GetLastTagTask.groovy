package ru.clevertec.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import ru.clevertec.commandExecutor.CommandExecutor
import ru.clevertec.commandExecutor.CommandExecutorImpl
import ru.clevertec.extension.GitTagExtension
import ru.clevertec.version.service.TagService

class GetLastTagTask extends DefaultTask {
    public static final String NAME = 'getLastTag'
    static final String GET_LAST_TAG = 'git tag'

    @Internal
    final CommandExecutor executor = new CommandExecutorImpl()

    @TaskAction
    void checkGitInstalled() {
        def gitTag = project.extensions.findByType(GitTagExtension)
        def tags = executor.execute(GET_LAST_TAG, 'Error retrieving tags', false)
                .split('\n')
                .toList()
        if (tags.isEmpty()) {
            println "No tags found"
            gitTag.setLastTag(Optional.empty())
            return
        }

        def lastTag = TagService.getLastVersion(tags)?.getVersion()
        println "Last tag: ${lastTag}"
        gitTag.setLastTag(Optional.of(lastTag))
    }
}
