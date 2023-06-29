/* import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
 */

public class VMWriter {
     private StringBuilder output = new StringBuilder();
    private String moduleName = "Main";
    private int labelCount = 0;
    private String outputFileName;
	private int callCount = 0;


    public VMWriter(String fname) {
        outputFileName = fname;
    }

    void setFileName(String s) {
        moduleName = s.substring(0, s.indexOf("."));
        moduleName = moduleName.substring(s.lastIndexOf("/") + 1);
        System.out.println(moduleName);
    }

    String segmentPointer(String segment, int index) {

        if (segment.equals("local"))
            return "LCL";
        if (segment.equals("argument"))
            return "ARG";
        if (segment.equals("this"))
            return "THIS";
        if (segment.equals("that"))
            return "THAT";
        if (segment.equals("pointer"))
            return "R" + (3 + index);
        if (segment.equals("temp"))
            return "R" + (5 + index);

        return moduleName + "." + index;
    }

    private void write(String s) {
        output.append(String.format("%s\n", s));
    }

     void writePush(String seg, int index) {
        if (seg.equals("constant")) {
            write("@" + index + " // push " + seg + " " + index);
            write("D=A");
            write("@SP");
            write("A=M");
            write("M=D");
            write("@SP");
            write("M=M+1");
        } else if (seg.equals("static") || seg.equals("temp") || seg.equals("pointer")) {
            write("@" + segmentPointer (seg, index) + " // push " + seg + " " + index);
            write("D=M");
            write("@SP");
            write("A=M");
            write("M=D");
            write("@SP");
            write("M=M+1");
        }

        else {
            write("@" + segmentPointer (seg, 0) + " // push " + seg + " " + index);
            //para local, argument, this e that
            write("D=M");
            write("@" + index);
            write("A=D+A");
            write("D=M");
            write("@SP");
            write("A=M");
            write("M=D");
            write("@SP");
            write("M=M+1");
        }
    }

    void writePop(String seg, int index) {
        if (seg.equals("static") || seg.equals("temp") || seg.equals("pointer")) {

            write("@SP // pop " + seg + " " + index);
            write("M=M-1");
            write("A=M");
            write("D=M");
            write("@" + segmentPointer (seg, index));
            write("M=D");
        } else {
            write("@" + segmentPointer (seg, 0) + " // pop " + seg + " " + index);
            write("D=M");
            write("@" + index);
            write("D=D+A");
            write("@R13");
            write("M=D");
            write("@SP");
            write("M=M-1");
            write("A=M");
            write("D=M");
            write("@R13");
            write("A=M");
            write("M=D");
        }
    }

}
