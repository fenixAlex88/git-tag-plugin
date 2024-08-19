package ru.clevertec.version.service

import org.gradle.api.GradleException
import ru.clevertec.version.Version

import java.util.regex.Pattern

class TagService {

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

    static String getVersion(Integer major, Integer minor, Integer patch, String suffix) {
        if (major == 0 && minor == 0 && !patch)
            return "No tags"
        return "v${major}.${minor}${patch ? ".${patch}" : ""}${suffix ? suffix : ''}"
    }

    static void parseVersion(String version, Version versionImpl) {
        def versionPattern = ~/(\d+)\.(\d+)(?:\.(\d+))?(-\w+)?/
        def matcher = versionPattern.matcher(version.replace('v', ''))

        if (!matcher.matches()) {
            throw new GradleException('Invalid tag format')
        }

        versionImpl.major = matcher.group(1).toInteger()
        versionImpl.minor = matcher.group(2).toInteger()
        versionImpl.patch = matcher.group(3) ? matcher.group(3).toInteger() : null
        versionImpl.suffix = matcher.group(4)
    }

    static String createNewVersion(String branch, String currentTag, Version versionImpl) {
        if (currentTag)
            return currentTag
        RULES.stream()
                .filter { rule -> rule.pattern.matcher(branch).matches() }
                .findFirst()
                .ifPresentOrElse(
                        { rule -> rule.action.call(versionImpl) },
                        { versionImpl.suffix = '-SNAPSHOT' }
                )
        return getVersion(versionImpl.major, versionImpl.minor, versionImpl.patch, versionImpl.suffix)
    }

    static Version getLastVersion(List<String> tags) {
        return tags.stream()
                .map(tag -> {
                    try {
                        Version version = new Version()
                        parseVersion(tag, version)
                        return version
                    } catch (Exception ignored) {
                        return null
                    }
                })
                .filter(version -> version != null)
                .max(Comparator.naturalOrder())
                .orElse(new Version())
    }

    private static class BranchRule {
        Pattern pattern
        Closure action

        BranchRule(Pattern pattern, Closure action) {
            this.pattern = pattern
            this.action = action
        }
    }
}
