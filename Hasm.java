/**
 * Assembler for the Hack computer from
 * The Elements of Computing Systems
 *
 * TODO: 
 *    possibly add more features (macros, preprocessor commands)
 */

import java.io.*;

class Hasm
{
    public static void main(String[] args)
    {
        // must specify file to assemble
        if (args.length != 1)
            Error.error("please specify a file to assemble");
        
        String inputFile = args[0];

        try {
            Assemble assembler = new Assemble(inputFile);
            assembler.assemble1();
            assembler.assemble2();
            assembler.close();
        } catch (FileNotFoundException ex)
        {
            Error.error("file \'" + inputFile + "\' not found");
        } catch (IOException ex)
        {
            Error.error("a i/o exception occured");
        }
    }
}
