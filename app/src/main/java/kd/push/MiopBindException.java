package kd.push;

public class MiopBindException extends Exception {
    public String r;

    public MiopBindException(String message, String r) {
        super(message);
        this.r = r;
    }
}
