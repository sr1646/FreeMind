package com.sr.va;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OutputWriter {
    private static PrintWriter printWriter;
    private static File logFile;
    static{
        try {
            logFile=new File("Log_"+new SimpleDateFormat("yyyy-MM-dd-HH-mm'.txt'").format(new Date()));
            printWriter  =new PrintWriter(logFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
