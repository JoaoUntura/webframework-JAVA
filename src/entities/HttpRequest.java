package entities;

import java.util.Map;

public class HttpRequest {

    private HttpMethod httpMethod;
    private String path;
    private String httpVersion;
    private String body;

    private Map<String, String> rawHeaders;

    private String user_agent;
    private String accept;
    private String cacheControl;
    private String host;
    private Long contentLength;

    public void setHttpRequestHeader(String header, String value){

        switch (header){
            case "Accept":
                setAccept(value);
                break;
            case "User-Agent":
                setUser_agent(value);
                break;
            case "Cache-Control":
                setCacheControl(value);
                break;
            case "Host":
                setHost(value);
                break;
            case "Content-Length":
                setContentLength(Long.valueOf(value));
                break;
        }

    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public Map<String, String> getRawHeaders() {
        return rawHeaders;
    }

    public void setRawHeaders(Map<String, String> rawHeaders) {
        this.rawHeaders = rawHeaders;
    }

    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getCacheControl() {
        return cacheControl;
    }

    public void setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    @Override
    public String toString() {
        return "HttpRequest{" +
                "httpMethod=" + httpMethod +
                ", path='" + path + '\'' +
                ", httpVersion='" + httpVersion + '\'' +
                ", body='" + body + '\'' +
                ", rawHeaders=" + rawHeaders +
                ", user_agent='" + user_agent + '\'' +
                ", accept='" + accept + '\'' +
                ", cacheControl='" + cacheControl + '\'' +
                ", host='" + host + '\'' +
                ", contentLength=" + contentLength +
                '}';
    }
}
