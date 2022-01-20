package sr.utility;

import static sr.utility.Output.decorateInSameLine;
import static sr.utility.Output.print;

public class Progress extends Thread{
    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    private boolean inProgress;

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        final int progressbarSize=5;
        final int animationSpeed=500;
        while (inProgress) {
            for (int i=1;i<progressbarSize+1;i++){
                sleep(animationSpeed);
                if(!inProgress){
                    break;
                }
                decorateInSameLine("0",progressbarSize-i);
                decorateInSameLine(" ",i);
                decorateInSameLine(" ",i);
                decorateInSameLine("0",progressbarSize-i);
                print("\r");
            }
            for (int i=1;i<progressbarSize+1;i++){
                sleep(animationSpeed);
                if(!inProgress){
                    break;
                }
                decorateInSameLine("0",i);
                decorateInSameLine(" ",progressbarSize-i);
                decorateInSameLine(" ",progressbarSize-i);
                decorateInSameLine("0",i);
                print("\r");
            }
        }               
    }
}




 









