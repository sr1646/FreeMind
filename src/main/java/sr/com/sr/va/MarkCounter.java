package sr.com.sr.va;

public class MarkCounter {
    private String studentName="sandip";

    /*
        Here
        1 = A
        2 = B
        1 = C
        1 = D
        5 = E   // MARK WILL NOT CUT FOR THIS
        6 = G   // GRACE MARK
     */
    private final int GRACE_MARK=6;
    private final int NO_MARK=5;

    private int answerkey[]={1, 4, 4, 2, 3, 1, 4, 4, 1, 1, 4, 3, 4, 3, 4, 2, 4, 3, 3, 3, 4, 1, 1, 2, 4, 1, 2, 2, 2, 3, 6, 4, 3, 1, 3, 3, 4, 1, 4, 3, 3, 3, 4, 4, 3, 2, 1, 3, 6, 4, 1, 1, 2, 4, 4, 1, 2, 1, 2, 4, 3, 4, 2, 6, 1, 6, 3, 3, 2, 4, 3, 2, 4, 4, 4, 1, 2, 1, 3, 3, 3, 1, 2, 4, 2, 4, 2, 4, 3, 2, 2, 4, 3, 4, 3, 2, 3, 1, 4, 1, 2, 1, 2, 4, 2, 3, 1, 1, 1, 1, 3, 1, 2, 2, 1, 1, 3, 1, 1, 4, 1, 1, 1, 3, 1, 2, 3, 1, 1, 1, 1, 4, 1, 2, 2, 2, 3, 2, 1, 2, 1, 1, 2, 6, 1, 2, 3, 1, 1, 3, 3, 1, 1, 4, 1, 3, 2, 1, 2, 3, 1, 3, 3, 2, 3, 3, 4, 1, 2, 2, 1, 2, 1, 3, 1, 2, 3, 2, 3, 2, 3, 1, 4, 2, 4, 1, 2, 4, 3, 3, 3, 4, 3, 1, 1, 2, 1, 2, 3, 1};

   private int sandip[]={5, 5, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 4, 5, 5, 5, 5, 5, 4, 5, 5, 5, 5, 5, 3, 5, 5, 1, 2, 4, 5, 5, 5, 5, 1, 1, 5, 5, 3, 5, 4, 5, 5, 5, 5, 5, 4, 4, 5, 2, 5, 5, 4, 1, 5, 1, 3, 4, 1, 4, 1, 4, 1, 5, 3, 3, 2, 4, 2, 2, 5, 5, 5, 1, 5, 5, 5, 1, 5, 5, 5, 4, 4, 4, 2, 4, 4, 5, 4, 4, 2, 4, 2, 2, 1, 5, 4, 3, 2, 1, 2, 4, 5, 3, 2, 1, 1, 1, 2, 2, 2, 2, 1, 1, 3, 1, 1, 4, 1, 1, 1, 3, 1, 2, 3, 1, 1, 1, 4, 4, 1, 2, 2, 2, 3, 2, 1, 2, 1, 3, 2, 4, 4, 2, 3, 1, 1, 3, 3, 1, 1, 4, 1, 3, 2, 1, 5, 1, 2, 3, 3, 2, 1, 1, 4, 3, 2, 2, 1, 2, 1, 3, 1, 5, 4, 4, 2, 2, 5, 5, 4, 2, 4, 1, 4, 4, 5, 4, 4, 4, 4, 2, 3, 5, 5, 2, 5, 4};


    //shantu below
    private int shnatu[]={1, 5, 2, 5, 5, 5, 2, 2, 2, 2, 5, 5, 4, 5, 4, 5, 2, 5, 5, 4, 1, 4, 4, 5, 3, 5, 4, 5, 2, 4, 2, 5, 5, 5, 3, 3, 5, 5, 5, 5, 3, 5, 5, 5, 5, 5, 5, 3, 3, 4, 4, 4, 1, 5, 4, 1, 1, 1, 2, 4, 3, 5, 2, 2, 1, 2, 3, 3, 2, 4, 5, 1, 3, 5, 5, 4, 2, 5, 5, 1, 5, 2, 4, 4, 4, 4, 4, 4, 4, 1, 1, 2, 2, 1, 1, 1, 5, 5, 1, 1, 2, 1, 2, 5, 2, 3, 2, 1, 1, 1, 2, 2, 1, 1, 1, 1, 3, 1, 1, 4, 1, 1, 1, 3, 1, 3, 3, 1, 1, 1, 4, 4, 1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 1, 2, 3, 3, 2, 1, 3, 3, 1, 1, 4, 1, 3, 4, 1, 2, 3, 2, 3, 3, 3, 2, 3, 4, 1, 2, 2, 1, 1, 2, 3, 1, 5, 4, 4, 3, 2, 1, 1, 4, 2, 4, 1, 1, 4, 5, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1};
    private int omrAnswers[]=sandip;
    public  int countMark(){
        int correctAnswer=0;
        int graceMark=0;
        int wrongAnswer=0;
        int notAttempted=0;
        for(int i=0;i<answerkey.length;i++){
            if(GRACE_MARK==answerkey[i]){
                graceMark++;
            }else if(omrAnswers[i]==answerkey[i] ){
                correctAnswer++;
            }else if(omrAnswers[i]==NO_MARK){
                notAttempted++;
            }else{
                wrongAnswer++;
            }
        }
        final int design=40;
        decorate("*",design);
        decorate("*",1);
        decorate("*",1);
        System.out.println("*\t\tName: "+studentName);
        System.out.println("*\t\tcorrectAnswer: "+correctAnswer);
        System.out.println("*\t\tgraceMark: "+graceMark);
        System.out.println("*\t\twrongAnswer: "+wrongAnswer);
        System.out.println("*\t\tnotAttempted: "+notAttempted);
        System.out.println("*");
        final int totalMark=correctAnswer+graceMark;
        System.out.println("*\t\ttotal correctAnswer+grace: "+totalMark);
        final double negativeMark=  wrongAnswer*0.25;
        System.out.println("*\t\ttotal cut wrongAnswer*0.25: "+negativeMark);
        System.out.println("*");
        final double finalResult=totalMark-negativeMark;
        System.out.println("*\t\tfinal Result: "+finalResult);
        decorate("*",1);
        decorate("*",1);
        decorate("*",design);
        return 0;
    }
    private void decorate(String decorator,int repeat){
        for(int i=0;i<repeat;i++)
            System.out.print(decorator);
        System.out.println();
    }


}
