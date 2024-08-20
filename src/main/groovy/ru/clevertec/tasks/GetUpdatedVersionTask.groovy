package ru.clevertec.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import ru.clevertec.extension.GitTagExtension
import ru.clevertec.version.Version

class GetUpdatedVersionTask extends DefaultTask {
    public static final String NAME = 'getUpdatedVersion'

    @TaskAction
    void getUpdatedVersion() {
        def gitTag = project.extensions.findByType(GitTagExtension)
        try {
            Version version = new Version()
            version.setVersion(gitTag.getLastTag().orElse("No tags"))
            def newTag = version.createNewVersion(
                    gitTag.getCurrentBranch().orElseThrow(() -> new GradleException("Current branch is not set")),
                    gitTag.getCurrentTag().orElse(null)
            )
            println "New tag: ${newTag}"
            gitTag.setNewTag(Optional.of(newTag))
        } catch (Exception e) {
            throw new GradleException('Error in tag name', e)
        }
    }
}
