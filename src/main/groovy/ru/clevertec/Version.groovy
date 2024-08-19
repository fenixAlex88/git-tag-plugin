package ru.clevertec

interface Version {
    String getVersion()
    void setVersion(String version)
    void parse(String version)
    String createNewVersion(String branch, boolean hasUncommittedChanges, String currentTag)
}