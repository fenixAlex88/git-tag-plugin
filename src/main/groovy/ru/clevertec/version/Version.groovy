package ru.clevertec.version

interface Version {
    String getVersion()
    void setVersion(String version)
    void parse(String version)
    String createNewVersion(String branch, String currentTag)
}