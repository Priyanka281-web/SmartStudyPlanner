package planner;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PlannerGUI extends JFrame implements ActionListener {

    JTextField nameField, daysField;
JTextField subjectField, difficultyField, chapterField;
JTextArea outputArea;
JButton generateBtn, addSubjectBtn;

java.util.ArrayList<Subject> subjectList = new java.util.ArrayList<>();
    public PlannerGUI(){
        setTitle("Smart Study Planner");
        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        add(new JLabel("Enter Name:"));
        nameField = new JTextField(20);
        add(nameField);

        add(new JLabel("Days Left For Exam:"));
        daysField = new JTextField(5);
        add(daysField);
        add(new JLabel("Subject Name:"));
subjectField = new JTextField(10);
add(subjectField);

add(new JLabel("Difficulty (Easy/Medium/Hard):"));
difficultyField = new JTextField(10);
add(difficultyField);

add(new JLabel("Chapters:"));
chapterField = new JTextField(5);
add(chapterField);

addSubjectBtn = new JButton("Add Subject");
add(addSubjectBtn);

        generateBtn = new JButton("Generate Study Plan");
        add(generateBtn);

        outputArea = new JTextArea(20,40);
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea));

        addSubjectBtn.addActionListener(this);
generateBtn.addActionListener(this);

        setVisible(true);
        loadSubjects();
        displaySubjects();
    }
    public void actionPerformed(ActionEvent e){

    // ---------- ADD SUBJECT BUTTON ----------
    if(e.getSource() == addSubjectBtn){
        try{
            String subName = subjectField.getText();
            String diff = difficultyField.getText();
            int ch = Integer.parseInt(chapterField.getText());

            subjectList.add(new Subject(subName, diff, ch));
            saveSubjects();
            outputArea.append("Added: " + subName + " (" + diff + ", " + ch + " chapters)\n");

            subjectField.setText("");
            difficultyField.setText("");
            chapterField.setText("");
        }
        catch(Exception ex){
            outputArea.append("Invalid subject input!\n");
        }
        return;
    }

    // ---------- GENERATE PLAN BUTTON ----------
    if(e.getSource() == generateBtn){
        try{
            String name = nameField.getText();
            int days = Integer.parseInt(daysField.getText());
            if(subjectList.size() == 0){
                outputArea.setText("Please add at least one subject first!");
                return;
            }
            Subject[] subjects = subjectList.toArray(new Subject[0]);

            String plan = Main.getPlan(name, subjects, days);
            outputArea.setText(plan);
        }
        catch(Exception ex){
            outputArea.setText("Please enter valid input!");
        }
    }
}
void saveSubjects(){
    try{
        java.io.BufferedWriter writer =
            new java.io.BufferedWriter(new java.io.FileWriter("subjects.txt"));

        for(Subject s : subjectList){
            writer.write(s.name + "," + s.difficulty + "," + s.chapters);
            writer.newLine();
        }

        writer.close();
    }
    catch(Exception e){
        outputArea.append("Error saving subjects!\n");
    }
}
void loadSubjects(){
    try{
        java.io.File file = new java.io.File("subjects.txt");

        if(!file.exists())
            return;

        java.util.Scanner reader = new java.util.Scanner(file);

        while(reader.hasNextLine()){
            String line = reader.nextLine();
            String parts[] = line.split(",");

            String name = parts[0];
            String diff = parts[1];
            int ch = Integer.parseInt(parts[2]);

            subjectList.add(new Subject(name, diff, ch));
            outputArea.append("Loaded: " + name + " (" + diff + ", " + ch + " chapters)\n");
        }

        reader.close();
    }
    catch(Exception e){
        outputArea.append("Error loading subjects!\n");
    }
}
void displaySubjects(){
    if(subjectList.size() == 0){
        outputArea.append("No subjects saved yet.\n");
        return;
    }

    outputArea.append("Previously Saved Subjects:\n");

    for(Subject s : subjectList){
        outputArea.append(s.name + " (" + s.difficulty + ") - " + s.chapters + " chapters\n");
    }

    outputArea.append("\n");
}
}