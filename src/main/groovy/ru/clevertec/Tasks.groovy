package ru.clevertec

enum Tasks {
    CHECK_GIT_INSTALLED('checkGitInstalled', 'Check if Git is installed'),
    CHECK_GIT_REPO('checkGitRepo', 'Check if the current directory is a Git repository'),
    CHECK_GIT_REMOTE_REPO('checkGitRemoteRepo', 'Check if the Git repository has a remote repository configured'),
    GET_CURRENT_BRANCH('getCurrentBranch', 'Get the current Git branch'),
    GET_LAST_TAG('getLastTag', 'Get the last Git tag'),
    CHECK_UNCOMMITTED_CHANGES('checkUncommitedChanges', 'Check for uncommitted changes in the Git repository'),
    GET_UPDATED_VERSION('getUpdatedVersion', 'Get the updated version based on the current state'),
    GET_CURRENT_TAG('getCurrentTag', 'Get the current Git tag'),
    ADD_TAG('addTag', 'Add a new Git tag'),
    ADD_REMOTE_TAG('addRemoteTag', 'Add a new Git tag and push it to the remote repository')

    final String taskName
    final String description

    Tasks(String taskName, String description) {
        this.taskName = taskName
        this.description = description
    }

    String getName() {
        return taskName
    }

    String getDescription() {
        return description
    }
}