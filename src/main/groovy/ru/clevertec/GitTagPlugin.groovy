package ru.clevertec

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.GradleException
import ru.clevertec.commandExecutor.CommandExecutor
import ru.clevertec.commandExecutor.CommandExecutorImpl
import ru.clevertec.version.VersionImpl
import ru.clevertec.taskBuilder.ExecuteTaskBuilderImpl
import ru.clevertec.taskExecutor.TaskExecutorImpl

class GitTagPlugin implements Plugin<Project> {

    static final String GIT_TAG_GROUP = 'git-tag'

    static final String CHECK_GIT_INSTALLED = 'git --version'
    static final String CHECK_GIT_REPO = 'git status'
    static final String GET_CURRENT_BRANCH = 'git branch --show-current'
    static final String GET_LAST_TAG = 'git tag'
    static final String GET_CURRENT_TAG = 'git describe --tags --exact-match'
    static final String CHECK_UNCOMMITTED_CHANGES = 'git status --porcelain'
    static final String SET_TAG = 'git tag '
    static final String CHECK_GIT_REMOTE_REPO = 'git remote'
    static final String SET_REMOTE_TAG = 'git push origin tag '

    String lastTag
    String currentTag
    String newTag
    String currentBranch
    String remoteRepo
    boolean hasUncommittedChanges

    CommandExecutor executor = new CommandExecutorImpl()

    @Override
    void apply(Project project) {

        new ExecuteTaskBuilderImpl(project, TaskNames.CHECK_GIT_INSTALLED)
                .withGroup(GIT_TAG_GROUP)
                .withExecutor(new TaskExecutorImpl({
                    executor.execute(CHECK_GIT_INSTALLED, 'Git not installed')
                    println "Git installed"
                }))
                .build()

        new ExecuteTaskBuilderImpl(project, TaskNames.CHECK_GIT_REPO)
                .withGroup(GIT_TAG_GROUP)
                .withExecutor(new TaskExecutorImpl({
                    executor.execute(CHECK_GIT_REPO, 'This is not a git repository.', true)
                    println 'Git repository exists'
                }))
                .dependsOn(TaskNames.CHECK_GIT_INSTALLED)
                .build()

        new ExecuteTaskBuilderImpl(project, TaskNames.GET_CURRENT_BRANCH)
                .withGroup(GIT_TAG_GROUP)
                .withExecutor(new TaskExecutorImpl({
                    currentBranch = executor.execute(GET_CURRENT_BRANCH, 'Error retrieving current branch', true)
                    println "Current branch: ${currentBranch}"
                }))
                .dependsOn(TaskNames.CHECK_GIT_REPO)
                .build()

        new ExecuteTaskBuilderImpl(project, TaskNames.GET_LAST_TAG)
                .withGroup(GIT_TAG_GROUP)
                .withExecutor(new TaskExecutorImpl({
                    def tags = executor.execute(GET_LAST_TAG, 'Error retrieving tags').split('\n').toList()
                    lastTag = VersionImpl.getLastVersion(tags).getVersion()
                    println "Last tag: ${lastTag}"
                }))
                .dependsOn(TaskNames.CHECK_GIT_REPO)
                .build()

        new ExecuteTaskBuilderImpl(project, TaskNames.CHECK_UNCOMMITTED_CHANGES)
                .withGroup(GIT_TAG_GROUP)
                .withExecutor(new TaskExecutorImpl({
                    def uncommittedChanges = executor.execute(CHECK_UNCOMMITTED_CHANGES, 'Error checking uncommitted changes')
                    hasUncommittedChanges = !uncommittedChanges.isEmpty()
                    if (hasUncommittedChanges) {
                       throw new GradleException("Uncommitted changes: ${lastTag}.uncommitted")
                    }
                    println "No uncommitted changes"
                }))
                .dependsOn(TaskNames.GET_LAST_TAG)
                .build()

        new ExecuteTaskBuilderImpl(project, TaskNames.GET_CURRENT_TAG)
                .withGroup(GIT_TAG_GROUP)
                .withExecutor(new TaskExecutorImpl({
                    currentTag = executor.execute(GET_CURRENT_TAG, 'Error retrieving current tag')
                    println currentTag ? "Current tag: ${currentTag}" : "No current tag"
                }))
                .dependsOn(TaskNames.CHECK_GIT_REPO)
                .build()

        new ExecuteTaskBuilderImpl(project, TaskNames.GET_UPDATED_VERSION)
                .withGroup(GIT_TAG_GROUP)
                .withExecutor(new TaskExecutorImpl({
                    try {
                        VersionImpl version = new VersionImpl()
                        version.setVersion(lastTag)
                        newTag = version.createNewVersion(
                                currentBranch,
                                currentTag
                        )
                        println "New tag: ${newTag}"
                    } catch (Exception e) {
                        throw new GradleException('Error in tag name', e)
                    }
                }))
                .dependsOn(
                        TaskNames.GET_CURRENT_BRANCH,
                        TaskNames.GET_LAST_TAG,
                        TaskNames.CHECK_UNCOMMITTED_CHANGES,
                        TaskNames.GET_CURRENT_TAG
                )
                .build()

        new ExecuteTaskBuilderImpl(project, TaskNames.ADD_TAG)
                .withGroup(GIT_TAG_GROUP)
                .withExecutor(new TaskExecutorImpl({
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
                    executor.execute(SET_TAG + newTag, "Error adding tag")
                    println "Tag \"${newTag}\" added"
                }))
                .dependsOn(TaskNames.GET_UPDATED_VERSION)
                .build()

        new ExecuteTaskBuilderImpl(project, TaskNames.CHECK_GIT_REMOTE_REPO)
                .withGroup(GIT_TAG_GROUP)
                .withExecutor(new TaskExecutorImpl({
                    remoteRepo = executor.execute(CHECK_GIT_REMOTE_REPO, 'This is not a git remote repository.', true)
                    println 'Git remote repository exists'
                }))
                .dependsOn(TaskNames.CHECK_GIT_REPO)
                .build()

        new ExecuteTaskBuilderImpl(project, TaskNames.ADD_REMOTE_TAG)
                .withGroup(GIT_TAG_GROUP)
                .withExecutor(new TaskExecutorImpl({
                    executor.execute(SET_REMOTE_TAG + newTag, "Error adding remote tag")
                    println "Tag \"${newTag}\" added on remote repository"

                }))
                .dependsOn(TaskNames.CHECK_GIT_REMOTE_REPO, TaskNames.ADD_TAG)
                .build()

    }
}