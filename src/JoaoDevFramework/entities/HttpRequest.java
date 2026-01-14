package JoaoDevFramework.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    private HttpMethod httpMethod;
    private String path;
    private String mainPath;
    private List<String> subPaths;
    private String httpVersion;
    private String body;

    private Map<String, String> rawHeaders;
    private Map<String, String> queryParams;
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

        if (path == null || path.isEmpty() || path.equals("/")) {
            return;
        }

        String cleanPath = path.startsWith("/") ? path.substring(1) : path;

        List<String> pathsStrings = new ArrayList<>(List.of(cleanPath.split("/")));

        if (pathsStrings.isEmpty()) return;

        int lastIndex = pathsStrings.size() - 1;

        String lastPath = pathsStrings.get(lastIndex);

        if (lastPath.contains("?")){

            StringBuilder cleanLastPath = new StringBuilder();
            StringBuilder queryParamsBuild = new StringBuilder();
            Map<String, String> queryParamsMap = new HashMap<>();
            String[] strings = lastPath.split("\\?");

            if(strings.length == 2){
                cleanLastPath.append(strings[0]);
                queryParamsBuild.append(strings[1]);

            }else if(strings.length > 2){

                List<String> stringList = new ArrayList<>(List.of(strings));
                cleanLastPath.append(stringList.subList(0, stringList.size() - 1).stream().reduce(String::concat).orElseThrow());
                queryParamsBuild.append(stringList.getLast());
            }

            List<String> queryParams = List.of(queryParamsBuild.toString().split("&"));

            for(String key_value:queryParams){

                String[] key_value_array = key_value.split("=");
                if(key_value_array.length != 2) continue;
                queryParamsMap.put(key_value_array[0], key_value_array[1]);

            }

            pathsStrings.add(lastIndex, cleanLastPath.toString());
            this.queryParams = queryParamsMap;


        }

        this.mainPath = pathsStrings.getFirst();
        this.subPaths = pathsStrings.size() > 0 ? pathsStrings.subList(1, lastIndex + 1) : new ArrayList<>();




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


    public String getMainPath() {
        return mainPath;
    }

    public List<String> getSubPaths() {
        return subPaths;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "httpMethod=" + httpMethod +
                ", path='" + path + '\'' +
                ", mainPath='" + mainPath + '\'' +
                ", subPaths=" + subPaths +
                ", httpVersion='" + httpVersion + '\'' +
                ", body='" + body + '\'' +
                ", rawHeaders=" + rawHeaders +
                ", queryParams=" + queryParams +
                ", user_agent='" + user_agent + '\'' +
                ", accept='" + accept + '\'' +
                ", cacheControl='" + cacheControl + '\'' +
                ", host='" + host + '\'' +
                ", contentLength=" + contentLength +
                '}';
    }
}
