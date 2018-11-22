package config;

public enum Service {
    VP("baidu", "http://www.baidu.com"),
    UCD("google", "http://www.google.com"),
    TEST("test", "https://httpbin.org");

    private final String prefix;
    private final String host;

    Service(String prefix, String host) {
        this.prefix = prefix;
        this.host = host;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getHost() {
        return host;
    }
}
