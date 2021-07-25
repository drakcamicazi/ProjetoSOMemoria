package process;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Process {
    private String processName;
    private int textSize;
    private int fixedDataSize;
    private int heapSize;
    private final int stackSize = 64;
    private int pid;
    private PageTable pageTable;

    public Process(String fileName){
        //this.setDescriptionFile(fileName);
        this.readFromFile(fileName);
        this.pageTable = new PageTable();
    }

    /*public Process(File fileName){
        this.setDescriptionFile(fileName);
        try {
            this.readFromFile(this.getDescriptionFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }*/

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setFixedDataSize(int fixedDataSize) {
        this.fixedDataSize = fixedDataSize;
    }

    public void setHeapSize(int heapSize) {
        this.heapSize = heapSize;
    }

    public String getProcessName() {
        return processName;
    }

    public int getTextSize() {
        return textSize;
    }

    public int getFixedDataSize() {
        return fixedDataSize;
    }

    public int getHeapSize() {
        return heapSize;
    }

    public int getStackSize() {
        return stackSize;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public PageTable getPageTable() {
        return pageTable;
    }

    /**
     * Le o arquivo com a descricao do processo e setta os atributos do processo a partir dos dados lidos
     * **/
    private void readFromFile(String fileName){

        List<String> lines;
        File inputFile;
        Scanner input;
        int i;
        String processName;
        int textSize;
        int fixedDataSize;

        i = 0;
        lines = new ArrayList<String>();

        inputFile= new File(fileName);
        try {
            input = new Scanner(inputFile);
            while(input.hasNextLine()){
                lines.add(input.nextLine());
                System.out.println(lines.get(i));
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        processName = lines.get(0).split("program ")[1];
        textSize = Integer.parseInt(lines.get(1).split("text ")[1]);
        fixedDataSize = Integer.parseInt(lines.get(2).split("data ")[1]);

        this.setProcessName(processName);
        this.setTextSize(textSize);
        this.setFixedDataSize(fixedDataSize);
    }

    /*private void setDescriptionFile(File processDescriptionFile) {
        this.descriptionFile = processDescriptionFile;
    }*/

    /*private void setDescriptionFile(String processName){
        File processDescriptionFile;
        if(processName.endsWith(".txt")){
            processDescriptionFile = new File(processName);
        }else{
            processDescriptionFile = new File(processName + ".txt");
        }
        this.descriptionFile = processDescriptionFile;
    }*/

    /*public File getDescriptionFile(){
        return this.descriptionFile;
    }*/

}
