package ru.clevertec

enum TaskName {
    CHECK_GIT_INSTALLED('checkGitInstalled'),
    CHECK_GIT_REPO('checkGitRepo'),
    GET_CURRENT_BRANCH('getCurrentBranch'),
    GET_LAST_TAG('getLastTag'),
    CHECK_UNCOMMITTED_CHANGES('CheckUncommitedChanges'),
    DETERMINE_VERSION('determineVersion'),
    GET_CURRENT_TAG('getCurrentTag'),
    ADD_TAG('addTag')

    final String taskName

    TaskName(String taskName) {
        this.taskName = taskName
    }

    String getName() {
        return taskName
    }
}