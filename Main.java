import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Main {
    private static String fromFile(File file) {        

        byte[] bytes;
        try {
            bytes = Files.readAllBytes(file.toPath());
            String textoDoArquivo = new String(bytes, "UTF-8");
            return textoDoArquivo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    } 

    private static void translateFile (File file, VMWriter code) {

        String input = fromFile(file);
        Parser p = new Parser(input);
        while (p.hasMoreCommands()) {
            var command = p.nextCommand();
            switch (command.Type) {
                // arithmetics
                case Command.Type.ADD:
                    code.writeArithmeticAdd();
                    break;

                case Command.Type.SUB:
                    code.writeArithmeticSub();
                    break;

                case Command.Type.NEG:
                    code.writeArithmeticNeg();
                    break;

                case Command.Type.NOT:
                    code.writeArithmeticNot();
                    break;
                
                case Command.Type.EQ:
                    code.writeArithmeticEq();
                    break;

                case Command.Type.LT:
                    code.writeArithmeticLt();
                    break;
                
                case Command.Type.GT:
                    code.writeArithmeticGt();
                    break;
                
                case Command.Type.AND:
                    code.writeArithmeticAnd();
                    break;

                case Command.Type.OR:
                    code.writeArithmeticOr();
                    break;


                case Command.Type.PUSH:
                    code.writePush(command.args.get(0), Integer.parseInt(command.args.get(1)));
                    break;
                
                case Command.Type.POP:
                    code.writePop(command.args.get(0), Integer.parseInt(command.args.get(1)));
                    break;

                default:
                    System.out.println(command.type.toString()+" not implemented");
            }
        } 
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Please provide a single file path argument.");
            System.exit(1);
        }

        File file = new File(args[0]);

        if (!file.exists()) {   
            System.err.println("The file doesn't exist.");
            System.exit(1);
        }

        // we need to compile every file in the directory
        if (file.isDirectory()) {

            var outputFileName = file.getAbsolutePath() +"/"+ file.getName()+".asm";
            System.out.println(outputFileName);
            VMWriter code = new VMWriter(outputFileName);

            code.writeInit();

            for (File f : file.listFiles()) {
                if (f.isFile() && f.getName().endsWith(".vm")) {

                    var inputFileName = f.getAbsolutePath();
                    var pos = inputFileName.indexOf('.');
                    
                    
                    System.out.println("compiling " +  inputFileName);
                    translateFile(f,code);

                }

            }
            code.save();
        // we only compile the single file
        } else if (file.isFile()) {
            if (!file.getName().endsWith(".vm"))  {
                System.err.println("Please provide a file name ending with .vm");
                System.exit(1);
            } else {
                var inputFileName = file.getAbsolutePath();
                var pos = inputFileName.indexOf('.');
                var outputFileName = inputFileName.substring(0, pos) + ".asm";
                VMWriter code = new VMWriter(outputFileName);
                System.out.println("compiling " +  inputFileName);
                code.writeInit();
                translateFile(file,code); 
                code.save();               
            }
        }
    }    
}