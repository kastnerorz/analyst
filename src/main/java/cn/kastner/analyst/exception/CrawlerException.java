package cn.kastner.analyst.exception;

public class CrawlerException extends Exception {

    private final String msg;

    public CrawlerException(String message) {
        super(message);
        msg = message;
    }

    public String getMsg() {
        return msg;
    }
}