package com.ques.copytodownload.utils;

import android.support.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceIdentifier {
    private static final String URL_PATTERN = "^(?:https?:)?(?://)?(?:[^@\\n]+@)?(?:www\\.)?([^:/\\n]+)";

    public enum ApiType {
        Instagram("instagram.com"),
        Twitter("twitter.com");

        public String domainName;

        ApiType(String domainName) {
            this.domainName = domainName;
        }

        public static ApiType fromDomainName(String domainName) {
            ApiType found = null;
            for (ApiType type: values()) {
                if (type.domainName.equalsIgnoreCase(domainName)) {
                    found = type;
                }
            }

            return found;
        }
    }

    private ServiceIdentifier() {
        throw new AssertionError("You MUST NOT create the instance of this class!!");
    }

    @Nullable
    public static ApiType getApiType(String url) {
        Pattern p = Pattern.compile(URL_PATTERN);
        Matcher m = p.matcher(url);
        if (!m.find()) {
            return null;
        }

        // m.group(0) returns full match, not capturing group
        String domain = m.group(1);
        return ApiType.fromDomainName(domain);
    }

    @Nullable
    public static Long parseTwitterStatusId(String url) {
        String pattern = ServiceIdentifier.URL_PATTERN.concat("\\/(\\w{1,15})\\/(?:status)\\/(\\d*)");

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(url);
        if (!m.find() || m.groupCount() < 3) {
            return null;
        }

        // m.group(0) returns full match, not capturing group
        String statusIdString = m.group(3);
        return Long.valueOf(statusIdString);
    }
}
