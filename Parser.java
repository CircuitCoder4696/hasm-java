/**
 * Parser module for Hack assembler
 *
 */

import java.io.*;

public class Parser
{
    // current command
    public String currentCommand = "";
    // file being assembled
    public String inputFile;
    // current line number in the file
    public int lineNumber = 0;
    // current line in file
    public String currentLine;

    // input file reader
    private BufferedReader fileReader;

    // command types
    enum CommandType {
        A_COMMAND,      // @xxx, where xxx is a decimal number or a symbol
        C_COMMAND,      // dest=comp;jump
        L_COMMAND       // (xxx), where xxx is a symbol
    }

    // open input file and get ready to parse it
    public Parser(String file) throws FileNotFoundException
    {
        inputFile = file;
        fileReader = new BufferedReader(new FileReader(file));
        lineNumber = 0;
    }

    // reads next command from input and makes it the curent command
    // returns true if command found
    // returns false at end of file
    public boolean advance() throws IOException
    {
        while (true)
        {
            currentLine = fileReader.readLine();
            lineNumber++;
            if (currentLine == null)
                return false;
            currentCommand = currentLine.replaceAll("//.*$", "").trim();
            if (currentCommand.equals(""))
                continue;

            return true;
        }
    }

    // returns the type of the current command
    // A_COMMAND for @xxx
    // C_COMMAND for dest=comp;jump
    // L_COMMAND for a label, (xxx)
    public CommandType commandType()
    {
        if (currentCommand.startsWith("@"))
        {
            return CommandType.A_COMMAND;
        } else if (currentCommand.startsWith("("))
        {
            return CommandType.L_COMMAND;
        } else
        {
            return CommandType.C_COMMAND;
        }
    }

    // returns symbol or decimal xxx of the current command
    // only applies to A_COMMAND or L_COMMAND
    public String symbol()
    {
        return currentCommand.substring(1).replace(")", "");
    }

    // returns dest field of current command (8 possibilities)
    // only applies to C_COMMAND
    public String dest()
    {
        String dest = "";
        if (currentCommand.contains("="))
        {
            String[] array = currentCommand.split("=");
            dest = array[0];
        }
        return dest;
    }

    // returns the comp field of the current command (28 possibilities)
    // only applies to C_COMMAND
    public String comp()
    {
        String comp;
        if (currentCommand.contains("="))
        {
            String[] array = currentCommand.split("=");
            String[] array1 = array[1].split(";");
            comp = array1[0];
        } else
        {
            String[] array = currentCommand.split(";");
            comp = array[0];
        }
        return comp;
    }

    // returns the jump field of the current command (8 possibilities)
    // only applies to C_COMMAND
    public String jump()
    {
        String jump = "";
        if (currentCommand.contains(";"))
        {
            String[] array = currentCommand.split(";");
            jump = array[1];
        }
        return jump;
    }

    // close input file
    public void close() throws IOException
    {
        fileReader.close();
        return;
    }
}
