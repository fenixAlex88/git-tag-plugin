package ru.clevertec

import org.gradle.api.Plugin
import org.gradle.api.Project

import ru.clevertec.extension.GitTagExtension
import ru.clevertec.tasks.AddRemoteTagTask
import ru.clevertec.tasks.AddTagTask
import ru.clevertec.tasks.CheckGitInstalledTask
import ru.clevertec.tasks.CheckGitRemoteRepoTask
import ru.clevertec.tasks.CheckGitRepoTask
import ru.clevertec.tasks.CheckUncommittedChangesTask
import ru.clevertec.tasks.GetCurrentBranchTask
import ru.clevertec.tasks.GetCurrentTagTask
import ru.clevertec.tasks.GetLastTagTask
import ru.clevertec.tasks.GetUpdatedVersionTask

class GitTagPlugin implements Plugin<Project> {

    static final String GIT_TAG_GROUP = 'git-tag'

    @Override
    void apply(Project project) {
        project.extensions.create('gitTag', GitTagExtension)

        project.tasks.register(Tasks.CHECK_GIT_INSTALLED.name, CheckGitInstalledTask) {
            group = GIT_TAG_GROUP
        }
        project.tasks.register(Tasks.CHECK_GIT_REPO.name, CheckGitRepoTask) {
            group = GIT_TAG_GROUP
            dependsOn(Tasks.CHECK_GIT_INSTALLED.name)
        }
        project.tasks.register(Tasks.GET_CURRENT_BRANCH.name, GetCurrentBranchTask) {
            group = GIT_TAG_GROUP
            dependsOn(Tasks.CHECK_GIT_REPO.name)
        }
        project.tasks.register(Tasks.GET_LAST_TAG.name, GetLastTagTask) {
            group = GIT_TAG_GROUP
            dependsOn(Tasks.CHECK_GIT_REPO.name)
        }
        project.tasks.register(Tasks.CHECK_UNCOMMITTED_CHANGES.name, CheckUncommittedChangesTask) {
            group = GIT_TAG_GROUP
            dependsOn(Tasks.CHECK_GIT_REPO.name, Tasks.GET_LAST_TAG.name)
        }
        project.tasks.register(Tasks.GET_CURRENT_TAG.name, GetCurrentTagTask) {
            group = GIT_TAG_GROUP
            dependsOn(Tasks.CHECK_GIT_REPO.name)
        }
        project.tasks.register(Tasks.GET_UPDATED_VERSION.name, GetUpdatedVersionTask) {
            group = GIT_TAG_GROUP
            dependsOn(
                    Tasks.GET_CURRENT_BRANCH.name,
                    Tasks.GET_LAST_TAG.name,
                    Tasks.CHECK_UNCOMMITTED_CHANGES.name,
                    Tasks.GET_CURRENT_TAG.name
            )
        }
        project.tasks.register(Tasks.ADD_TAG.name, AddTagTask) {
            group = GIT_TAG_GROUP
            dependsOn(Tasks.GET_UPDATED_VERSION.name)
        }
        project.tasks.register(Tasks.CHECK_GIT_REMOTE_REPO.name, CheckGitRemoteRepoTask) {
            group = GIT_TAG_GROUP
            dependsOn(Tasks.CHECK_GIT_REPO.name)
        }
        project.tasks.register(Tasks.ADD_REMOTE_TAG.name, AddRemoteTagTask) {
            group = GIT_TAG_GROUP
            dependsOn(Tasks.CHECK_GIT_REMOTE_REPO.name, Tasks.ADD_TAG.name)
        }
    }
}