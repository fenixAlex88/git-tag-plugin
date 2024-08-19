package ru.clevertec

enum TaskNames {
    CHECK_GIT_INSTALLED('checkGitInstalled'),
    CHECK_GIT_REPO('checkGitRepo'),
    CHECK_GIT_REMOTE_REPO('checkGitRemoteRepo'),
    GET_CURRENT_BRANCH('getCurrentBranch'),
    GET_LAST_TAG('getLastTag'),
    CHECK_UNCOMMITTED_CHANGES('checkUncommitedChanges'),
    GET_UPDATED_VERSION('getUpdatedVersion'),
    GET_CURRENT_TAG('getCurrentTag'),
    ADD_TAG('addTag'),
    ADD_REMOTE_TAG('addRemoteTag')

    final String taskName

    TaskNames(String taskName) {
        this.taskName = taskName
    }

    String getName() {
        return taskName
    }
}