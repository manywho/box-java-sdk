package com.box.sdk;

import java.net.MalformedURLException;
import java.net.URL;

class QueryStringBuilder {
    private final StringBuilder stringBuilder;

    QueryStringBuilder() {
        this.stringBuilder = new StringBuilder();
    }

    QueryStringBuilder(String existing) {
        if (existing == null) {
            this.stringBuilder = new StringBuilder();
        } else {
            this.stringBuilder = new StringBuilder(existing);
        }
    }

    QueryStringBuilder appendParam(String key, String... fields) {
        StringBuilder valuesBuilder = new StringBuilder();
        for (String field : fields) {
            valuesBuilder.append(field);
            valuesBuilder.append(",");
        }
        valuesBuilder.deleteCharAt(valuesBuilder.length() - 1);

        this.appendParam(key, valuesBuilder.toString());
        return this;
    }

    QueryStringBuilder appendParam(String key, String value) {
        if (this.stringBuilder.length() == 0) {
            this.stringBuilder.append('?');
        } else {
            this.stringBuilder.append('&');
        }

        this.stringBuilder.append(key);
        this.stringBuilder.append('=');
        this.stringBuilder.append(this.encode(value));
        return this;
    }

    QueryStringBuilder appendParam(String key, long value) {
        return this.appendParam(key, Long.toString(value));
    }

    URL addToURL(URL existing) throws MalformedURLException {
        String existingQuery = existing.getQuery();
        if (existingQuery == null || existingQuery.equals("")) {
            return new URL(existing.toString() + this.toString());
        }

        return new URL(existing.toString().replace(existingQuery, this.toString()));
    }

    @Override
    public String toString() {
        return this.stringBuilder.toString();
    }

    private String encode(String unencoded) {
        StringBuilder encodedBuilder = new StringBuilder();
        for (int i = 0, n = unencoded.length(); i < n; i++) {
            char c = unencoded.charAt(i);
            switch (c) {
                case ' ':
                    encodedBuilder.append('+');
                    break;
                case '!':
                    encodedBuilder.append("%21");
                    break;
                case '"':
                    encodedBuilder.append("%22");
                    break;
                case '#':
                    encodedBuilder.append("%23");
                    break;
                case '$':
                    encodedBuilder.append("%24");
                    break;
                case '&':
                    encodedBuilder.append("%26");
                    break;
                case '\'':
                    encodedBuilder.append("%27");
                    break;
                case '(':
                    encodedBuilder.append("%28");
                    break;
                case ')':
                    encodedBuilder.append("%29");
                    break;
                case '+':
                    encodedBuilder.append("%2b");
                    break;
                case ',':
                    encodedBuilder.append("%2c");
                    break;
                case '/':
                    encodedBuilder.append("%2f");
                    break;
                case ':':
                    encodedBuilder.append("%3a");
                    break;
                case ';':
                    encodedBuilder.append("%3b");
                    break;
                case '=':
                    encodedBuilder.append("%3d");
                    break;
                case '?':
                    encodedBuilder.append("%3f");
                    break;
                case '@':
                    encodedBuilder.append("%40");
                    break;
                case '[':
                    encodedBuilder.append("%5b");
                    break;
                case ']':
                    encodedBuilder.append("%5d");
                    break;
                case '{':
                    encodedBuilder.append("%7b");
                    break;
                case '}':
                    encodedBuilder.append("%7d");
                    break;
                default:
                    encodedBuilder.append(c);
                    break;
            }
        }

        return encodedBuilder.toString();
    }
}
