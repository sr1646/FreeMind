package sr.basic;

public class Series {
    public String getSeriese(){
//        Scanner stdin=new Scanner(System.in);
//        System.out.println("Enter number");
        int n=8;
        final int start=1;
        int seriese=start;
        int supportingSeriese=3;
        final int supportingSerieseChange=2;
        StringBuilder generatedSeriese=new StringBuilder();
        for(int i=0;i<n;i++){
            generatedSeriese.append(seriese).append(", ");
            seriese+=supportingSeriese;
            supportingSeriese+=supportingSerieseChange;
        }
        return generatedSeriese.toString();
    }
}
