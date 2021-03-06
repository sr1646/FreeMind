package sr.utility;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Output {
    static Progress  progressThread;
    public static void printLine(Object message) {
        doPrint(message);
    }
    private static final String exceptionFile="logOfException.txt";
    private static FileHelper fileHelper=new FileHelper();
    public static void print(Object message) {
        if(message ==null){
            System.out.print("null");
        }else{
            System.out.print(message.toString());
        }
    }
    private static void doPrint(Object message) {
        if(message ==null){
            System.out.println("null");
        }else{
            System.out.println(message.toString());
        }
    }

    public static void debug(String message) {
        doPrint(message);
    }
    public static String exceptionStacktraceToString(Exception e)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        e.printStackTrace(ps);
        ps.close();
        return baos.toString();
    }
    public static void exception(Exception e) {
        fileHelper.writeFile(e.getMessage(),exceptionFile);
        fileHelper.writeFile(exceptionStacktraceToString(e),exceptionFile);
    }
    public static void decorate(String decorator) {
        decorate(decorator,20);
    }
    public static void decorate(String decorator, int repeatTill) {
        StringBuilder decoration=new StringBuilder(repeatTill+2);
        decoration.append("\n");
        for(int i=0;i<repeatTill;i++){
            decoration.append(decorator);
        }
        decoration.append("\n");
        printLine(decoration);
    }
    public static void decorateInSameLine(String decorator, int repeatTill) {
        StringBuilder decoration=new StringBuilder(repeatTill);
        for(int i=0;i<repeatTill;i++){
            decoration.append(decorator);
        }
        print(decoration);
    }
    public static void drawProgressBar(int workDone, int totalWork) {
        final int HUNDRED_PERCENT=100;
        int percentage= workDone *HUNDRED_PERCENT/ totalWork;

        print("[ ");
        decorateInSameLine("#",(percentage));
        decorateInSameLine(" ",(HUNDRED_PERCENT-percentage));
        print(" ] ");
        print(percentage+"% done\r");
    }
    public static  void startProgress(){
        progressThread=new Progress();
        progressThread.setInProgress(true);
        progressThread.start();
    }
    public static void stopProgress(){
        progressThread.setInProgress(false);
    }



}
