package ASU.CAIE.GUI;

/**
 * Separate launcher entry point.
 * Some build tools / JavaFX module configurations require the class
 * with {@code main()} to NOT extend {@code Application} directly.
 * This thin wrapper delegates to {@link UMSPortal}.
 */
public class Main {
    public static void main(String[] args) {
        UMSPortal.main(args);
    }
}