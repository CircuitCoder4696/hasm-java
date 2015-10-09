/**
 * Error handling for the Hack assembler
 *
 */

class Error
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
}
