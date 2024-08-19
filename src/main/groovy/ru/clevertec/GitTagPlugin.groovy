package ru.clevertec

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.clevertec.VersionImpl.VersionImpl
import ru.clevertec.taskBuilder.ExecuteTaskBuilder
import ru.clevertec.taskExecutorImpl.CustomTaskExecutor
import ru.clevertec.taskExecutorImpl.GitCommandExecutor

class GitTagPlugin implements Plugin<Project> {

    static final String GIT_TAG_GROUP = 'git-tag'

    static final String CHECK_GIT_INSTALLED = 'git --version'
    static final String CHECK_GIT_REPO = 'git status'
    static final String GET_CURRENT_BRANCH = 'git branch --show-current'
    static final String GET_LAST_TAG = 'git tag'
    static final String GET_CURRENT_TAG = 'git describe --tags --exact-match'
    static final String CHECK_UNCOMMITTED_CHANGES = 'git status --porcelain'

    @Override
    void apply(Project project) {
        new ExecuteTaskBuilder(project, TaskName.CHECK_GIT_INSTALLED)
                .withGroup(GIT_TAG_GROUP)
                .withExecutor(new GitCommandExecutor(CHECK_GIT_INSTALLED, 'Git installed', 'Git is not installed. Please install Git and try again.'))
                .build()

        new ExecuteTaskBuilder(project, TaskName.CHECK_GIT_REPO)
                .withGroup(GIT_TAG_GROUP)
                .withExecutor(new GitCommandExecutor(CHECK_GIT_REPO, 'Git repository found', 'This is not a git repository.'))
                .dependsOn(TaskName.CHECK_GIT_INSTALLED)
                .build()

        new ExecuteTaskBuilder(project, TaskName.GET_CURRENT_BRANCH)
                .withGroup(GIT_TAG_GROUP)
                .withExecutor(new CustomTaskExecutor({ proj ->
                    def branch = GET_CURRENT_BRANCH.execute().text.trim()
                    proj.ext.currentBranch = branch
                    println "Current branch: ${branch}"
                }))
                .dependsOn(TaskName.CHECK_GIT_REPO)
                .build()

        new ExecuteTaskBuilder(project, TaskName.GET_LAST_TAG)
                .withGroup(GIT_TAG_GROUP)
                .withExecutor(new CustomTaskExecutor({ proj ->
                    def tags = GET_LAST_TAG.execute().text.trim().split('\n').toList()
                    def lastVersion = VersionImpl.getLastVersion(tags)
                    proj.ext.lastTag = lastVersion.getVersion()
                    println "Last tag: ${proj.ext.lastTag}"
                }))
                .dependsOn(TaskName.CHECK_GIT_REPO)
                .build()

        new ExecuteTaskBuilder(project, TaskName.CHECK_UNCOMMITTED_CHANGES)
                .withGroup(GIT_TAG_GROUP)
                .withExecutor(new CustomTaskExecutor({ proj ->
                    def uncommittedChanges = CHECK_UNCOMMITTED_CHANGES.execute().text.trim()
                    proj.ext.hasUncommittedChanges = !uncommittedChanges.isEmpty()
                    println "${proj.ext.hasUncommittedChanges ? "There are" : "No"} uncommitted changes"
                }))
                .dependsOn(TaskName.CHECK_GIT_REPO)
                .build()

        new ExecuteTaskBuilder(project, TaskName.GET_CURRENT_TAG)
                .withGroup(GIT_TAG_GROUP)
                .withExecutor(new CustomTaskExecutor({ proj ->
                    proj.ext.currentTag = GET_CURRENT_TAG.execute().text.trim()
                }))
                .dependsOn(TaskName.CHECK_GIT_REPO)
                .build()

        new ExecuteTaskBuilder(project, TaskName.DETERMINE_VERSION)
                .withGroup(GIT_TAG_GROUP)
                .withExecutor(new CustomTaskExecutor({ proj ->
                    try {
                        Version version = new VersionImpl()
                        version.setVersion(proj.ext.lastTag as String)
                        def newVersion = version.createNewVersion(
                                proj.ext.currentBranch as String,
                                proj.ext.hasUncommittedChanges as boolean,
                                proj.ext.currentTag as String
                        )
                        proj.ext.newVersion = newVersion
                        println "New version: ${newVersion}"
                    } catch (Exception e) {
                        throw new GradleException('Error in tag name', e)
                    }
                }))
                .dependsOn(
                        TaskName.GET_CURRENT_BRANCH,
                        TaskName.GET_LAST_TAG,
                        TaskName.CHECK_UNCOMMITTED_CHANGES,
                        TaskName.GET_CURRENT_TAG
                )
                .build()

        new ExecuteTaskBuilder(project, TaskName.ADD_TAG)
                .withGroup(GIT_TAG_GROUP)
                .withExecutor(new CustomTaskExecutor({ proj ->
                    if (proj.ext.hasUncommittedChanges) {
                        println "Build version with uncommitted changes: ${proj.ext.newVersion}.uncommitted"
                        return
                    }
                    if (proj.ext.currentTag) {
                        println "Current state already has a tag: ${proj.ext.currentTag}. No new tag will be created."
                        return
                    }
                    if (!proj.ext.has('newVersion')) {
                        throw new GradleException('newVersion property is not set. Ensure determineVersion task is executed.')
                    }
                    def newVersion = proj.ext.newVersion
                    if (newVersion != '.uncommitted') {
                        "git tag ${newVersion}".execute().waitFor()
                    }
                }))
                .dependsOn(TaskName.DETERMINE_VERSION)
                .build()
    }
}