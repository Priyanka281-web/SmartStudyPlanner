package planner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("===== SMART STUDY PLANNER =====");
        showPreviousPlan();
        Subject[] subjectList = loadSubjects();
        if(subjectList != null){
    System.out.print("Previous subjects detected. Reuse them? (yes/no): ");
    String choice = sc.nextLine();

    if(!choice.equalsIgnoreCase("yes")){
        subjectList = createSubjects();
    }
}
else{
    subjectList = createSubjects();
}
        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        System.out.print("Enter number of days left for exam: ");
        int days = sc.nextInt();

        generatePlan(name, subjectList, days);
        trackProgress(subjectList);

    }

    static void generatePlan(String name, Subject[] subjects, int days){
        try{
    BufferedWriter writer = new BufferedWriter(new FileWriter("studyplan.txt", true));

    BufferedWriter subjectWriter = new BufferedWriter(new FileWriter("subjects.txt"));
    // Save subjects data
for(int i = 0; i < subjects.length; i++){
    subjectWriter.write(subjects[i].name + "," + subjects[i].difficulty + "," + subjects[i].chapters);
    subjectWriter.newLine();
}


    writer.newLine();
    writer.write("================================");
    writer.newLine();

        System.out.println("\nHello " + name + " 👋");
        System.out.println("Your Personalized Study Plan:\n");
        writer.write("Hello " + name);
        writer.newLine();
        writer.write("Your Personalized Study Plan:");
        writer.newLine();
        writer.newLine();

        // Sorting subjects based on difficulty priority
for(int i = 0; i < subjects.length - 1; i++){
    for(int j = i + 1; j < subjects.length; j++){

        if(getPriority(subjects[i].difficulty) < getPriority(subjects[j].difficulty)){
            Subject temp = subjects[i];
            subjects[i] = subjects[j];
            subjects[j] = temp;
        }
    }
}
int totalChapters = 0;

// count total chapters
for(int i = 0; i < subjects.length; i++){
    totalChapters += subjects[i].chapters;
}

int chaptersPerDay = Math.max(1, totalChapters / days);
if(totalChapters > days){
    System.out.println("\n⚠ Heavy syllabus detected!");
    System.out.println("You need to study about " + chaptersPerDay + " chapters per day to finish on time.\n");
}
int currentSubject = 0;
int currentChapter = 1;

for(int d = 1; d <= days; d++){

    System.out.println("Day " + d + ":");
    writer.write("Day " + d + ":");
    writer.newLine();

    int studiedToday = 0;

    while(studiedToday < chaptersPerDay && currentSubject < subjects.length){

        Subject sub = subjects[currentSubject];

        System.out.println("   Study -> " + sub.name + " Chapter " + currentChapter);
        writer.write("   Study -> " + sub.name + " Chapter " + currentChapter);
        writer.newLine();

        currentChapter++;
        studiedToday++;

        if(currentChapter > sub.chapters){
            currentSubject++;
            currentChapter = 1;
        }
    }

    System.out.println();
}
        writer.close();
        subjectWriter.close();
System.out.println("\nStudy plan saved to studyplan.txt");
}
catch(IOException e){
    e.printStackTrace();
}

    }
    static int getPriority(String difficulty){

    difficulty = difficulty.toLowerCase();

    if(difficulty.equals("hard"))
        return 3;
    else if(difficulty.equals("medium"))
        return 2;
    else
        return 1;
}
static void trackProgress(Subject[] subjects){

    Scanner sc = new Scanner(System.in);
    int completed = 0;

    System.out.println("\n--- Daily Progress Check ---");

    for(int i = 0; i < subjects.length; i++){
        System.out.print("Did you complete " + subjects[i].name + "? (yes/no): ");
        String ans = sc.nextLine();

        if(ans.equalsIgnoreCase("yes")){
            completed++;
        }
    }

    double percent = ((double)completed / subjects.length) * 100;

    System.out.println("\nProgress: " + completed + "/" + subjects.length + " subjects completed");
    System.out.println("Completion Percentage: " + percent + "%");

    if(percent < 50)
        System.out.println("⚠ You are behind schedule. Study harder!");
    else
        System.out.println("Good progress! Keep going 👍");
}
static void showPreviousPlan(){

    File file = new File("studyplan.txt");



    if(file.exists()){
        System.out.println("\nPrevious Study Plan Found:\n");

        try{
            Scanner reader = new Scanner(file);

            while(reader.hasNextLine()){
                System.out.println(reader.nextLine());
            }

            reader.close();
        }
        catch(Exception e){
            System.out.println("Unable to read previous plan.");
        }
    }
}
static Subject[] loadSubjects(){

    File file = new File("subjects.txt");

    if(!file.exists())
        return null;

    try{
        Scanner reader = new Scanner(file);
        ArrayList<Subject> list = new ArrayList<>();

        while(reader.hasNextLine()){
            String line = reader.nextLine();
            String parts[] = line.split(",");

            if(parts.length == 3){
    String name = parts[0];
    String diff = parts[1];
    int ch = Integer.parseInt(parts[2]);
    list.add(new Subject(name, diff, ch));
}
        }

        reader.close();

        if(list.size() == 0)
            return null;

        Subject[] arr = new Subject[list.size()];
        return list.toArray(arr);
    }
    catch(Exception e){
        return null;
    }
}
static Subject[] createSubjects(){

    System.out.print("Enter number of subjects: ");
    int subjects = sc.nextInt();
    sc.nextLine(); // clear buffer

    Subject[] subjectList = new Subject[subjects];

    for(int i = 0; i < subjects; i++){

        System.out.print("Enter subject name: ");
        String subName = sc.nextLine();

        System.out.print("Enter difficulty (Easy/Medium/Hard): ");
        String diff = sc.nextLine();

        System.out.print("Enter number of chapters: ");
        int ch = sc.nextInt();
        sc.nextLine();

        subjectList[i] = new Subject(subName, diff, ch);
    }

    return subjectList;
}

}  
