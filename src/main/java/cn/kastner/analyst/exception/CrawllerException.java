package cn.kastner.analyst.exception;

public class CrawllerException extends Exception {

    private final String msg;

    public CrawllerException(String message) {
        super(message);
        msg = message;
    }

    public String getMsg() {
        return msg;
    }
}