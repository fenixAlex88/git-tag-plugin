package ru.clevertec.version

import ru.clevertec.version.service.TagService

class Version extends AbstractVersion implements Comparable<Version> {
    Integer major = 0
    Integer minor = 0
    Integer patch
    String suffix

    @Override
    String getVersion() {
        return TagService.getVersion(major, minor, patch, suffix)
    }

    @Override
    void setVersion(String version) {
        if (version && version != "No tags") {
            TagService.parseVersion(version, this)
        }
    }

    @Override
    void parse(String version) {
        TagService.parseVersion(version, this)
    }

    @Override
    String createNewVersion(String branch, String currentTag) {
        return TagService.createNewVersion(branch, currentTag, this)
    }

    @Override
    int compareTo(Version other) {
        if (this.major != other.major) {
            return Integer.compare(this.major, other.major)
        }
        if (this.minor != other.minor) {
            return Integer.compare(this.minor, other.minor)
        }
        if (this.patch && other.patch) {
            return Integer.compare(this.patch, other.patch)
        }
        return this.patch ? 1 : -1
    }
}