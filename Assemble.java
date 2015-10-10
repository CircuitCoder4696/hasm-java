/**
 * Assemble class for the Hack assembler
 *
 */

import java.io.*;
import java.util.*;

public class Assemble
{
    // input and output files
    private String inputFile;
    private PrintWriter out;
    // symbol table for variables / labels
    private SymbolTable table = new SymbolTable();

    public Assemble(String file) throws IOException
    {
        // define input / output files
        inputFile = file;
        String outputFile = inputFile.replaceAll("\\..*", "") + ".hack";
        out = new PrintWriter(new FileWriter(outputFile));
        // initialize symbol table
        table.initialize();
    }

    // first pass of the assembler
    // go through file building the symbol table
    // only labels are handled, variables are handeled in the 2nd pass
    public void assemble1() throws FileNotFoundException, IOException
    {
        Parser parser = new Parser(inputFile);
        int romAddress = 0;
        String symbol;

        while (parser.advance())
        {
            if (parser.commandType() == Parser.CommandType.L_COMMAND)
            {
                symbol = parser.symbol();
                if (!table.contains(symbol))
                    table.addEntry(symbol, romAddress);
            } else
            {
                romAddress++;
            }
        }
        parser.close();
        return;
    }

    // 2nd pass of the assembler
    // handle variables
    // generate code, replace symbols with values from symbol table
    public void assemble2() throws FileNotFoundException, IOException
    {
        Parser parser = new Parser(inputFile);
        String dest, comp, jump;
        String symbol, value;
        // starting address for variables
        int ramAddress = 16;

        while (parser.advance())
        {
            try
            {
                if (parser.commandType() == Parser.CommandType.C_COMMAND)
                {
                    dest = parser.dest();
                    comp = parser.comp();
                    jump = parser.jump();
                
                    out.println("111" + Code.comp(comp) + Code.dest(dest) + Code.jump(jump));
                } else if (parser.commandType() == Parser.CommandType.A_COMMAND)
                {
                    symbol = parser.symbol();
                    if (Character.isDigit(symbol.charAt(0)))
                    {
                        value = Code.toBinary(symbol);
                    }
                    else if (table.contains(symbol))
                    {
                        value = Integer.toString(table.getAddress(symbol));
                        value = Code.toBinary(value);
                    }
                    else
                    {
                        table.addEntry(symbol, ramAddress);
                        value = Code.toBinary("" + ramAddress);
                        ramAddress++;
                    }

                    out.println("0" + value);
                }
            }
            catch (InvalidDestException ex) {
                Error.error("Invalid destination", inputFile, parser.lineNumber, parser.currentLine);
            }
            catch (InvalidCompException ex) {
                Error.error("Invalid computation", inputFile, parser.lineNumber, parser.currentLine);
            }
            catch (InvalidJumpException ex) {
                Error.error("Invalid jump", inputFile, parser.lineNumber, parser.currentLine);
            }
        }
        parser.close();
        return;
    }

    // close output file
    public void close() throws IOException
    {
        out.close();
        return;
    }
}
