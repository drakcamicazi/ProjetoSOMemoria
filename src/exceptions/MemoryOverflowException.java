package exceptions;

public class MemoryOverflowException extends Throwable {
    public MemoryOverflowException(String s) {
        System.out.println(s);
    }
}
