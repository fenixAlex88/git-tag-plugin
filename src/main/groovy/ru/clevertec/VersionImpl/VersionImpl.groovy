package ru.clevertec.VersionImpl

import org.gradle.api.GradleException
import ru.clevertec.Version

class VersionImpl implements Version {
    private Integer major
    private Integer minor
    private Integer patch
    private String suffix

    @Override
    String getVersion() {
        return "v${major}.${minor}${patch ? ".${patch}" : ""}${suffix ? suffix : ''}"
    }

    @Override
    void setVersion(String version) {
        parse(version)
    }

    @Override
    void parse(String version) {
        def versionPattern = ~/(\d+)\.(\d+)(?:\.(\d+))?(-\w+)?/
        def matcher = versionPattern.matcher(version.replace('v', ''))

        if (!matcher.matches()) {
            throw new GradleException('Неверный формат тега')
        }

        major = matcher.group(1).toInteger()
        minor = matcher.group(2).toInteger()
        patch = matcher.group(3) ? matcher.group(3).toInteger() : null
        suffix = matcher.group(4)
    }

    @Override
    String createNewVersion(String branch, boolean hasUncommittedChanges, String currentTag ) {
        println branch
        println hasUncommittedChanges
        println currentTag
        if (hasUncommittedChanges || currentTag) {
            return getVersion()
        }
        switch (branch) {
            case ~/dev|qa/:
                if (patch != null) {
                    patch++
                } else {
                    minor++
                }
                suffix = ''
                break
            case ~/stage/:
                if (patch != null) {
                    patch++
                } else {
                    minor++
                }
                suffix = '-rc'
                break
            case ~/master/:
                major++
                minor = 0
                patch = null
                suffix = ''
                break
            default:
                suffix = '-SNAPSHOT'
        }

        if (hasUncommittedChanges) {
            suffix += '.uncommitted'
        }

        return getVersion()
    }

    static Version getLastVersion(List<String> tags) {
        VersionImpl lastVersion = new VersionImpl()
        tags.each { tag ->
            try {
                VersionImpl currentVersion = new VersionImpl()
                currentVersion.setVersion(tag)
                if (currentVersion.isGreaterThan(lastVersion)) {
                    lastVersion = currentVersion
                }
            } catch (GradleException ignore) {
                println "Tag: ${tag} ignored as incorect"
            }
        }
        return lastVersion
    }

    boolean isGreaterThan(VersionImpl other) {
        if (this.major != other.major) {
            return this.major > other.major
        }
        if (this.minor != other.minor) {
            return this.minor > other.minor
        }
        if (this.patch && other.patch) {
            return this.patch > other.patch
        }
        return this.patch && !other.patch
    }
}
