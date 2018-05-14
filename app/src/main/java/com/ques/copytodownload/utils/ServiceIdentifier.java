package com.ques.copytodownload.utils;

import android.support.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceIdentifier {
    public enum ApiType {
        Instagram("instagram.com");

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
        Pattern p = Pattern.compile("^(?:https?:)?(?://)?(?:[^@\\n]+@)?(?:www\\.)?([^:/\\n]+)");
        Matcher m = p.matcher(url);
        if (!m.find()) {
            return null;
        }

        // m.group(0) returns full match, not capturing group
        String domain = m.group(1);
        return ApiType.fromDomainName(domain);
    }
}
