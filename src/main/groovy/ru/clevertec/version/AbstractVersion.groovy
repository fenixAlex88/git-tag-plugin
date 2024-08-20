package ru.clevertec.version

abstract class AbstractVersion {

    abstract String getVersion()

    abstract void setVersion(String version)

    abstract void parse(String version)

    abstract String createNewVersion(String branch, String currentTag)
}