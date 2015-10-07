/**
 * Assemble class for the Hack assembler
 *
 * TODO: fix error handling, possibly add more features (macros, preprocessor commands)
 *
 */

import java.io.*;
import java.util.*;

public class Assemble
{
    // throw an error, print message and filename/line number if applicable
    public static void error(String message, String fileName, int lineNum)
    {
        System.out.println("Error: " + fileName + ":" + lineNum + ":\n" + message);
        System.exit(1);
    }
    public static void error(String message)
    {
        System.out.println("Error: " + message);
        System.exit(1);
    }

    // first pass of the assembler
    // go through file building the symbol table
    // only labels are handled, variables are handeled in the 2nd pass
    public static void assemble1(String inputFile, SymbolTable table) throws FileNotFoundException, IOException
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

        return;
    }

    // 2nd pass of the assembler
    // handle variables
    // generate code, replace symbols with values from symbol table
    public static void assemble2(String inputFile, SymbolTable table) throws FileNotFoundException, IOException
    {
        Parser parser = new Parser(inputFile);
        String dest, comp, jump;
        String symbol, value;
        // starting address for variables
        int ramAddress = 16;

        while (parser.advance())
        {
            if (parser.commandType() == Parser.CommandType.C_COMMAND)
            {
                dest = parser.dest();
                comp = parser.comp();
                jump = parser.jump();
                
                System.out.println("111" + Code.comp(comp) + Code.dest(dest) + Code.jump(jump));
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

                System.out.println("0" + value);
            }
        }
        
        return;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        // must specify file to assemble
        if (args.length != 1)
            error("please specify a file to assemble");
        
        // input file, get ready to parse
        String inputFile = args[0];
        // create and initialize a symbol table
        SymbolTable table = new SymbolTable();
        table.initialize();
        // output file
        String outputFile = inputFile.replaceAll("\\..*", "") + ".hack";

        assemble1(inputFile, table);
        assemble2(inputFile, table);
    }
}
