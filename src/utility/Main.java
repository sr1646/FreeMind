package utility;

import basic.Series;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utility.Output.printLine;

public class Main {
    private void showMenu(){
        for(Task task:Task.values()){
            printLine(task.ordinal()+" "+task);
            printLine("\t"+task.getDescription()+"\n");
        }
        printLine("Enter your choice");
    }
    public void driver(){
        showMenu();
//        Task task = getTaskFromStandardInput();
        Task task=Task.SEPARATE_VIDEO_AND_PHOTO ;
//        print("Selected Task: "+task);

        switch (task){
            case SHOW_ALL_FOLDER:
                new FileHelper().showAllFolder("E:\\google_photos\\do_experiments\\4gb\\test1");
                break;
            case GET_ALL_LINK_FROM_FILE:
                    getAllLink();
                break;
            case ADD_VALUE_IN_SET:
                addValueInSet();
                break;
            case MOVE_ALL_FILES_TO_FOLDER:
//                FileHelper.moveFiles("E:\\AS\\experiment\\source","E:\\AS\\experiment\\destination");
                moveFiles();
                break;
            case DEVIDE_DATA_FOR_DVD:
                devideDvdCompatible();
                break;
            case DVD_PART_TO_FOLDER:
                DVDDataToFolder();
                break;
            case GENERATE_SERIESE:
                System.out.println(new Series().getSeriese());
                break;
            case SHOW_SIZE_OF_FOLDERS:
                showFolderSize("C:\\as\\experiment\\output");
                break;
            case SHOW_ALL_TYPE_OF_FILE_FROM_FOLDER:
                showTypesFromFolder("C:\\as\\experiment\\test");
                break;
            case SEPARATE_VIDEO_AND_PHOTO:
                moveSpecificTypeFile("C:\\as\\experiment\\test","C:\\as\\experiment\\vedeo",".mp4");
                break;
            default :
                throw new IllegalStateException("Unexpected value: " + task);
        }
    }

    private void moveSpecificTypeFile(String sourceFolder, String destinationFolderStr, String typeOfFile) {
        new FileHelper().moveAllTypes(sourceFolder, destinationFolderStr, typeOfFile);
    }

    private void showTypesFromFolder(String sourceFolderStr) {
        printLine(new FileHelper().getAllTypes(sourceFolderStr));
    }


    private void DVDDataToFolder() {
        String sourcePath="C:\\as\\experiment\\output"
                ,destinationPath="C:\\as\\experiment\\test";
//        Scanner standardInput=new Scanner(System.in);
//        print("Enter source folder:");
//        sourcePath=standardInput.nextLine();
//        print("Enter destination folder:");
//        destinationPath=standardInput.nextLine();
        new FileHelper().DVDFilesToFolder(sourcePath,destinationPath);
    }

    private void showFolderSize(String sourceFolde) {
        new FileHelper().showFileFolerWithSize(sourceFolde);
    }

    private void devideDvdCompatible() {

        String sourcePath="C:\\as\\experiment\\test"
                ,destinationPath="C:\\as\\experiment\\output";
//        Scanner standardInput=new Scanner(System.in);
//        print("Enter source folder:");
//        sourcePath=standardInput.nextLine();
//        print("Enter destination folder:");
//        destinationPath=standardInput.nextLine();
       new FileHelper().devideFilesForDVD(sourcePath,destinationPath);
    }

    private void moveFiles() {
        Scanner standardInput=new Scanner(System.in);
        String sourcePath,destinationPath;
        printLine("Enter source folder:");
        sourcePath=standardInput.nextLine();
        printLine("Enter destination folder:");
        destinationPath=standardInput.nextLine();
        new FileHelper().moveFiles(sourcePath,destinationPath);
    }

    private Task getTaskFromStandardInput() {
        Scanner standardInput=new Scanner(System.in);
//        int choice=-1;
        int choice=standardInput.nextInt();

        if(choice< 0 || choice>=Task.values().length){
            printLine("Invalid choice: "+choice+", no such task exist");
            System.exit(1);
        }
        Task task=Task.values()[choice];
        return task;
    }

    private void addValueInSet() {
        Scanner standardInput=new Scanner(System.in);
        int set=1;
        Set<String> unique = addValueInSetFirstTime(standardInput, set++);
        nextSetCompare(standardInput, set++,unique);

    }

    private Set<String> addValueInSetFirstTime(Scanner standardInput, int set) {
        InputUtil choice = getInputFromConsoleOrFile(standardInput, "Do you want to provide input for set "+ set +" from MasterMind.txt?");

        Set<String> unique=new LinkedHashSet<>();
        switch (choice){
            case READ_FROM_FILE:
                List<String> fileData = new FileHelper().readFile("MasterMind.txt");
                for (String line : fileData) {
                    unique.add(line);
                }
                break;

            case READ_FROM_CONSOLE:
                final String terminator="astop";
                printLine("Enter "+terminator+" anywhere to stop inputting values");
                do{
                    String input= standardInput.nextLine().trim();
                    if(terminator.equalsIgnoreCase(input)){
                        break;
                    }
                    unique.add(input);
                }while(true);
                break;
        }
        return unique;
    }
    private Set<String> nextSetCompare(Scanner standardInput, int set, Set<String> unique) {
        printLine("Success fully added values in set");
        final int DISPLAY_IN_SORTED_ORDER=1;
        final int COMPARE_WITH_OTHER_SET=2;
        printLine("\n1 For display value in sorted order");
        printLine("2 For compare this set with another set i.e. program will say value exist or not as well as provide new values list separately");

        int nextOperation=Integer.valueOf(standardInput.nextInt());

        switch (nextOperation){
            case DISPLAY_IN_SORTED_ORDER:
                List<String> sortedList=new ArrayList( Arrays.asList(unique));
                Collections.sort(sortedList);
                printLine(sortedList);
                break;
            case COMPARE_WITH_OTHER_SET:
                break;
            default:
                printLine("Invalid Input so displaying data in sorted order");
                List<String> sortedList1=new ArrayList( Arrays.asList(unique));
                Collections.sort(sortedList1);
                printLine(sortedList1);
        }

        InputUtil choice = getInputFromConsoleOrFile(standardInput, "Do you want to provide input for set "+ set +" from MasterMind.txt?");

        switch (choice){
            case READ_FROM_FILE:
                List<String> fileData = new FileHelper().readFile("MasterMind.txt");
                for (String line : fileData) {
                    unique.add(line);
                }
                break;

            case READ_FROM_CONSOLE:
                final String terminator="astop";
                printLine("Enter "+terminator+" anywhere to stop inputting values");
                do{
                    String input= standardInput.nextLine().trim();
                    if(terminator.equalsIgnoreCase(input)){
                        break;
                    }
                    unique.add(input);
                }while(true);
                break;
        }
        return unique;
    }
    private InputUtil getInputFromConsoleOrFile(Scanner standardInput, String message) {
        int choice=-1;
        boolean wrongChoice=false;
        do{
            if (wrongChoice){
                printLine("\nInvalid choice, Please try again.");
            }
            printLine(message);
            for(InputUtil iu:InputUtil.values()){
                printLine(iu.ordinal()+" "+iu);
            }
            choice= Integer.valueOf(standardInput.nextLine());
            wrongChoice=true;
        }while(choice<0 || choice>=InputUtil.values().length);
        return InputUtil.values()[choice];
    }

    private void getAllLink() {
        List<String> fileData=new FileHelper().readFile("MasterMind.txt");
        Pattern p = Pattern.compile("(\"http).*\"");
        List<String> allLinks = new ArrayList<>(100);
        for(String line:fileData){
            Matcher m = p.matcher(line);
            allLinks.addAll(getAllMatch(m));
        }
        printLine("total links: "+allLinks.size());
        printLine(allLinks);
    }

    public List<String> getAllMatch(Matcher matcher) {
        List<String> result = new ArrayList<String>();
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }



    private static void mapExample() {
        Map<String,Long> map=new HashMap<String,Long>();
        map.put("3",1522L);
        map.put("4",1523L);
        map.put("1",153L);
        map.put("5",15123L);
        map.put("2",152L);
        map.put("6",1153L);

        Map<String,Long> sortedMap= new MapSort().sortMapByValue(map);

        for (Object entry : sortedMap.entrySet()){
            Map.Entry  entryv=(Map.Entry<String,Long>)entry;
            System.out.println("Key = " + entryv.getKey() +
                    ", Value = " + entryv.getValue());
        }
    }

    private static void sortArray(int[] array) {
        int minValue,minIndex;
        for(int i = 0; i< array.length; i++){
            minValue= array[i];
            minIndex=i;
            for(int j = i+1; j< array.length; j++){
                if(minValue> array[j]){
                    minIndex=j;
                    minValue= array[j];
                }
            }
            if(minIndex==i){
                //do nothing
            }else{
                array[minIndex]= array[i];
                array[i]=minValue;
            }
        }

    }
    public static void main(String[] args)
    {
        new Main().driver();
    }

}
