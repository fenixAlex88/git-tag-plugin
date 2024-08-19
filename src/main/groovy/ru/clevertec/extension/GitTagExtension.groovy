package ru.clevertec.extension

interface GitTagExtension {
    String getLastTag()
    void setLastTag(String lastTag)

    String getCurrentTag()
    void setCurrentTag(String currentTag)

    String getNewTag()
    void setNewTag(String newTag)

    String getCurrentBranch()
    void setCurrentBranch(String currentBranch)

    String getRemoteRepo()
    void setRemoteRepo(String remoteRepo)

    boolean getHasUncommittedChanges()
    void setHasUncommittedChanges(boolean hasUncommittedChanges)
}