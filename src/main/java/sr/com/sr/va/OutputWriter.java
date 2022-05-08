package sr.com.sr.va;

import sr.utility.DateUtil;
import sr.utility.Output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class OutputWriter {
    private static PrintWriter printWriter;
    private static File logFile;
    static{
        try {
            logFile=new File("Log_"+ DateUtil.getNowDateForFileName()+".txt");
            printWriter  =new PrintWriter(logFile);
        } catch (FileNotFoundException e) {
            Output.exception(e);
        }
    }
    public static void printLogFileNameToConsole(){
        System.out.println("\nyou can find log file here: "+logFile.getAbsolutePath());
    }
    public static void write(Object o){
        String str=o.toString();
        System.out.print(str);
        printWriter.write(str);
        printWriter.flush();
    }

}
