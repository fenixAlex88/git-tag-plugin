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

        project.tasks.register(Tasks.CHECK_GIT_INSTALLED.getName(), CheckGitInstalledTask) {
            group = GIT_TAG_GROUP
            description = Tasks.CHECK_GIT_INSTALLED.getDescription()
        }
        project.tasks.register(Tasks.CHECK_GIT_REPO.getName(), CheckGitRepoTask) {
            group = GIT_TAG_GROUP
            description = Tasks.CHECK_GIT_REPO.getDescription()
            dependsOn(Tasks.CHECK_GIT_INSTALLED.getName())
        }
        project.tasks.register(Tasks.GET_CURRENT_BRANCH.getName(), GetCurrentBranchTask) {
            group = GIT_TAG_GROUP
            description = Tasks.GET_CURRENT_BRANCH.getDescription()
            dependsOn(Tasks.CHECK_GIT_REPO.getName())
        }
        project.tasks.register(Tasks.GET_LAST_TAG.getName(), GetLastTagTask) {
            group = GIT_TAG_GROUP
            description = Tasks.GET_LAST_TAG.getDescription()
            dependsOn(Tasks.CHECK_GIT_REPO.getName())
        }
        project.tasks.register(Tasks.CHECK_UNCOMMITTED_CHANGES.getName(), CheckUncommittedChangesTask) {
            group = GIT_TAG_GROUP
            description = Tasks.CHECK_UNCOMMITTED_CHANGES.getDescription()
            dependsOn(Tasks.CHECK_GIT_REPO.getName(), Tasks.GET_LAST_TAG.getName())
        }
        project.tasks.register(Tasks.GET_CURRENT_TAG.getName(), GetCurrentTagTask) {
            group = GIT_TAG_GROUP
            description = Tasks.GET_CURRENT_TAG.getDescription()
            dependsOn(Tasks.CHECK_GIT_REPO.getName())
        }
        project.tasks.register(Tasks.GET_UPDATED_VERSION.getName(), GetUpdatedVersionTask) {
            group = GIT_TAG_GROUP
            description = Tasks.GET_UPDATED_VERSION.getDescription()
            dependsOn(
                    Tasks.GET_CURRENT_BRANCH.getName(),
                    Tasks.GET_LAST_TAG.getName(),
                    Tasks.CHECK_UNCOMMITTED_CHANGES.getName(),
                    Tasks.GET_CURRENT_TAG.getName()
            )
        }
        project.tasks.register(Tasks.ADD_TAG.getName(), AddTagTask) {
            group = GIT_TAG_GROUP
            description = Tasks.ADD_TAG.getDescription()
            dependsOn(Tasks.GET_UPDATED_VERSION.getName())
        }
        project.tasks.register(Tasks.CHECK_GIT_REMOTE_REPO.getName(), CheckGitRemoteRepoTask) {
            group = GIT_TAG_GROUP
            description = Tasks.CHECK_GIT_REMOTE_REPO.getDescription()
            dependsOn(Tasks.CHECK_GIT_REPO.getName())
        }
        project.tasks.register(Tasks.ADD_REMOTE_TAG.getName(), AddRemoteTagTask) {
            group = GIT_TAG_GROUP
            description = Tasks.ADD_REMOTE_TAG.getDescription()
            dependsOn(Tasks.CHECK_GIT_REMOTE_REPO.getName(), Tasks.ADD_TAG.getName())
        }
    }
}