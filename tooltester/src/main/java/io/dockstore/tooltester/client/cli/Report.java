package io.dockstore.tooltester.client.cli;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.stream.Collectors;

import static io.dockstore.tooltester.client.cli.ExceptionHandler.IO_ERROR;
import static io.dockstore.tooltester.client.cli.ExceptionHandler.exceptionMessage;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author gluu
 * @since 23/01/17
 */
public abstract class Report implements Closeable {
    static final CharSequence COMMA_SEPARATOR = ",";
    static final CharSequence TAB_SEPARATOR = "\t";
    public BufferedWriter writer;

    Report(String name) {
        List<String> header = getHeader();
        try {

            File file = new File(name);
            this.writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), UTF_8));
        } catch (FileNotFoundException e) {
            exceptionMessage(e, "Cannot create new file", IO_ERROR);
        }
        printAndWriteLine(header);
    }

    public abstract List<String> getHeader();

    public void printAndWriteLine(List<String> string) {
        printLine(string);
        writeLine(string);
    }

    public void close() {
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            exceptionMessage(e, "Could not close file", IO_ERROR);
        }
    }

    void writeLine(List<String> values) {
        String value2 = values.stream().map(Object::toString).collect(Collectors.joining(COMMA_SEPARATOR));
        try {
            writer.append(value2);
            writer.append("\n");
        } catch (IOException e) {
            exceptionMessage(e, "Cannot write to file", IO_ERROR);
        }
    }

    public abstract void printLine(List<String> string);
}
