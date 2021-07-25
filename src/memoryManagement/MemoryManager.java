package memoryManagement;

import process.PageTable;

import java.util.HashMap;
import java.util.Map;

public class MemoryManager implements ManagementInterface{

    private int pidCounter;
    private int numberActiveProcess;
    private Map<Integer, PageTable> pageTablesList;
    private Map<Integer, String> processList;

    public MemoryManager(int numFrames){
        this.pidCounter = 1;
        this.numberActiveProcess = 0;
        this.processList = new HashMap<Integer, String>();
        this.pageTablesList = new HashMap<Integer, PageTable>();
    }

    @Override
    public int loadProcessToMemory(String processName) {
        return 0;
    }

    @Override
    public int allocateMemoryToProcess(int processId, int size) {
        return 0;
    }

    @Override
    public int freeMemoryFromProcess(int processId, int size) {
        return 0;
    }

    @Override
    public void excludeProcessFromMemory(int processId) {

    }

    @Override
    public void resetMemory() {

    }

    @Override
    public int getPhysicalAddress(int processId, int logicalAddress) {
        return 0;
    }

    @Override
    public String getBitMap() {
        return null;
    }

    @Override
    public String getPageTable(int processId) {
        return null;
    }

    @Override
    public String[] getProcessList() {
        String[] result = new String[this.numberActiveProcess];
        int i = 0;
        for (Integer pid : processList.keySet() ) {
            result[i] = "PID: " + pid + ", process name: " + this.processList.get(pid);
            i++;
        }
        return result;

    }
}
