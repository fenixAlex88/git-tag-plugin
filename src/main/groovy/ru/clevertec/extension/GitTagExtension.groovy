package ru.clevertec.extension

interface GitTagExtension {
    Optional<String> getLastTag();
    void setLastTag(Optional<String> lastTag);

    Optional<String> getCurrentTag();
    void setCurrentTag(Optional<String> currentTag);

    Optional<String> getNewTag();
    void setNewTag(Optional<String> newTag);

    Optional<String> getCurrentBranch();
    void setCurrentBranch(Optional<String> currentBranch);

    boolean getHasUncommittedChanges();
    void setHasUncommittedChanges(boolean hasUncommittedChanges);
}