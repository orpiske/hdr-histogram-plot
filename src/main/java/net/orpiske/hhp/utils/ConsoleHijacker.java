package net.orpiske.hhp.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Console hijacker utility. This abstracts the logic required to redirect stdout. It is useful
 * because some clients output the performance data to the standard output. This allows the code
 * to hijack that data and save it according to maestro format.
 */
public class ConsoleHijacker {

    private static ConsoleHijacker instance = null;
    private PrintStream oldOut = null;
    private OutputStream stream;

    private ConsoleHijacker() {

    }


    /**
     * Gets the hijacker instance
     * @return An instance of the hijacker
     */
    public synchronized static ConsoleHijacker getInstance() {
        if (instance == null) {
            instance = new ConsoleHijacker();
        }

        return instance;
    }


    /**
     * Starts the hijacker
     * @return true if started successfully or false if a hijacking is already in progress
     */
    public boolean start() {
        if (oldOut != null) {
            return false;
        }

        stream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(stream);

        oldOut = System.out;
        System.setOut(ps);
        return true;
    }


    /**
     * Starts the hijacker
     * @param stream output stream to use (ie.: FileOutputStream to print straight to a file)
     * @return true if started successfully or false if a hijacking is already in progress
     */
    public boolean start(final OutputStream stream) {
        if (oldOut != null) {
            return false;
        }

        this.stream = stream;
        PrintStream ps = new PrintStream(stream);

        oldOut = System.out;
        System.setOut(ps);
        return true;
    }


    /**
     * Stops the hijacking.
     * @return The standard output contents or null if nothing was captured
     */
    public String stop() {
        if (stream == null) {
            return null;
        }

        System.out.flush();

        String ret = stream.toString();
        try {
            stream.close();
        } catch (IOException e) {

        }

        System.setOut(oldOut);
        return ret;
    }
}
