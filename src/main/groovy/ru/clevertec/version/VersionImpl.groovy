package ru.clevertec.version

import org.gradle.api.GradleException

import java.util.regex.Pattern

class VersionImpl implements Version, Comparable<VersionImpl> {
    Integer major = 0
    Integer minor = 0
    Integer patch
    String suffix

    private static final List<BranchRule> RULES = [
            new BranchRule(~/(dev|qa)/, { version ->
                version.patch = null
                version.minor++
                version.suffix = ''
            }),
            new BranchRule(~/(stage)/, { version ->
                version.patch = null
                version.minor++
                version.suffix = '-rc'
            }),
            new BranchRule(~/(master)/, { version ->
                version.major++
                version.minor = 0
                version.patch = null
                version.suffix = ''
            })
    ]

    @Override
    String getVersion() {
        if (major == 0 && minor == 0 && !patch)
            return "No tags"
        return "v${major}.${minor}${patch ? ".${patch}" : ""}${suffix ? suffix : ''}"
    }

    @Override
    void setVersion(String version) {
        if (version && version != "No tags") {
            parse(version)
        }
    }

    @Override
    void parse(String version) {
        def versionPattern = ~/(\d+)\.(\d+)(?:\.(\d+))?(-\w+)?/
        def matcher = versionPattern.matcher(version.replace('v', ''))

        if (!matcher.matches()) {
            throw new GradleException('Invalid tag format')
        }

        major = matcher.group(1).toInteger()
        minor = matcher.group(2).toInteger()
        patch = matcher.group(3) ? matcher.group(3).toInteger() : null
        suffix = matcher.group(4)
    }

    @Override
    String createNewVersion(String branch, String currentTag) {
        if (currentTag)
            return currentTag
        RULES.stream()
                .filter { rule -> rule.pattern.matcher(branch).matches() }
                .findFirst()
                .ifPresentOrElse(
                        { rule -> rule.action.call(this) },
                        { this.suffix = '-SNAPSHOT' }
                )
        return getVersion()
    }

    @Override
    int compareTo(VersionImpl other) {
        if (this.major != other.major) {
            return Integer.compare(this.major, other.major);
        }
        if (this.minor != other.minor) {
            return Integer.compare(this.minor, other.minor);
        }
        if (this.patch && other.patch) {
            return Integer.compare(this.patch, other.patch);
        }
        return this.patch ? 1 : -1;
    }

    private static class BranchRule {
        Pattern pattern
        Closure action

        BranchRule(Pattern pattern, Closure action) {
            this.pattern = pattern
            this.action = action
        }
    }

    static Version getLastVersion(List<String> tags) {
        return tags.stream()
                .map(tag -> {
                    try {
                        VersionImpl version = new VersionImpl();
                        version.setVersion(tag);
                        return version;
                    } catch (Exception ignored) {
                        return null;
                    }
                })
                .filter(version -> version != null)
                .max(Comparator.naturalOrder())
                .orElse(new VersionImpl());
    }
}