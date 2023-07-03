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
}