package me.vwinter.library.command;

public class WrappedSender {

    private final Object sender;

    public WrappedSender(Object sender) {
        this.sender = sender;
    }

    public <T> T get() {
        try {
            return (T) this.sender;
        } catch (Exception e) {
            return null;
        }
    }

}
