package memoryManagement;

import exceptions.MemoryOverflowException;
import process.PageTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import process.Process;

public class MemoryManager implements ManagementInterface{

    /**
     * @param pidCounter contador incrementado a cada carregamento de processo para a memoria, identificando unicamente aquele processo
     * @param numberActiveProcess numero de processos ativos naquele momento (programas carregados na memoria)
     * @param pageTablesList mapeia o PID de cada processo para sua respectiva tabela de pagina
     * @param processList mapeia o PID de cada processo para seu respectivo nome
     * @param bitMap vetor onde cada posicao referencia um quadro de memoria. O valor 1 em dada posicao indica que o quadro esta ocupado, e 0 o oposto.
     * @param numFreeFrames indica o numero de quadros totalmente livres
     * @param pageSize = tamanho de cada pagina nas tabelas de pagina, em bytes. Equivalente ao tamanho de um quadro.
     * **/
    private int pidCounter;
    private int numberActiveProcess;
    private Map<Integer, PageTable> pageTablesList;
    private Map<Integer, String> processList;
    private int[] bitMap;
    int numFreeFrames;
    int numFrames;

    final int pageSize = 32;

    public MemoryManager(int numFrames){
        this.numFrames = numFrames;
        this.pidCounter = 1;
        this.numberActiveProcess = 0;
        this.processList = new HashMap<Integer, String>();
        this.pageTablesList = new HashMap<Integer, PageTable>();
        this.bitMap = new int[numFrames];
        this.numFreeFrames = numFrames;

        this.setBitMapInitialState();
    }

    /**
     * Inicia o bitMap com todos os quadros livres (valor 0 em todas posições)
     * **/
    private void setBitMapInitialState(){
        for(int i = 0; i < this.numFrames; i++){
            this.bitMap[i] = 0;
        }
    }

    @Override
    public int loadProcessToMemory(String processName) throws MemoryOverflowException {
        Process process;
        PageTable processPageTable;
        int processPid;
        int processTextSize;
        int processDataSize;
        int stackSize;
        int amountOfFramesNeeded;

        process = new Process(processName);
        processPageTable = process.getPageTable();
        processPid = pidCounter;
        process.setPid(processPid);
        processTextSize = process.getTextSize();
        processDataSize = process.getFixedDataSize();
        stackSize = process.getStackSize();
        amountOfFramesNeeded = (int)Math.ceil((processTextSize + processDataSize + stackSize)/32.0);

        if(amountOfFramesNeeded > numFreeFrames){
            throw new MemoryOverflowException("Sem memoria disponivel para carregar o processo " + processPid + ":" + processName);
        }

        this.firstFit(process);

        this.processList.put(processPid,processName);
        this.pageTablesList.put(processPid, processPageTable);

        pidCounter++;
        return processPid;
    }

    private void firstFit(Process process) {
        //recebe, do process, o tamanho dos segmentos a serem alocados:
        int textSize = process.getTextSize();
        int dataSize = process.getFixedDataSize();
        int stackSize = process.getStackSize();
        PageTable processPageTable = process.getPageTable();

        int endOfAHole;
        int holeSize;
        //criacao de uma lista de segmentos ainda nao alocados:
        ArrayList<Integer> notAllocatedList = new ArrayList<Integer>();
        notAllocatedList.add(textSize);
        notAllocatedList.add(stackSize);
        notAllocatedList.add(dataSize);

        int begginingOfAHole = 0; //posicao inicial
        int i = 0;
        //enquanto houver segmentos nao alocados e nao chegar ao fim do bitmap:
        while (!notAllocatedList.isEmpty() && i < numFrames) {
            if (this.bitMap[i] == 0) {
                continue;
            } else {
                endOfAHole = i; //percorre bitMap ate achar um bit invalido, e marca o fim de um buraco
            }

            holeSize = endOfAHole - begginingOfAHole + 1;
            for (int size : notAllocatedList) { //para cada tamanho de segmento ainda nao alocado
                if (size <= holeSize) { //testa se o segmento cabe no buraco
                    boolean foo = notAllocatedList.remove(new Integer(size)); //se sim, remove da lista de nao alocados


                    /*
                     *
                     * Inserir aqui a logica de alocacao do segmento de tamanho = size
                     * da posicao begginingOfAHole do bitmap, ate a posicao endOfAHole
                     * preencher desde begginingOfAHole ate endOfAHole do bitmap com 1
                     * mapear esses mesmos quadros para a processPageTable
                     *
                     * */

                    System.out.println("Alocou: " + size);
                    break; //para de percorrer a lista de nao alocados
                }
            }
            while (this.bitMap[i] == 1) { //busca o proximo bit valido para posicionar novamente o begginingOfAHole
                i++;
            }
            begginingOfAHole = i;
            i++;
        }

        //chegamos no final do bitmap, testando para todos os buracos do bitmap se os segmentos cabiam la
        if(!notAllocatedList.isEmpty()){

            /*
            *
            * Se ainda assim existem segmentos que nao foram alocados, alocar eles para quadros nao continuos mesmo
            *
            * */
        }
    }

    @Override
    public int allocateMemoryToProcess(int processId, int size) {

        PageTable pageTable = pageTablesList.get(processId); //recupera a PageTable dessse processo

        //...

        this.numberActiveProcess += 1;
        return 0;
    }

    @Override
    public int freeMemoryFromProcess(int processId, int size) {

        PageTable pageTable = pageTablesList.get(processId); //recupera a PageTable dessse processo
        return 0;
    }

    @Override
    public void excludeProcessFromMemory(int processId) {
        PageTable pageTable = pageTablesList.get(processId); //recupera a PageTable dessse processo

        /*
        *  pageTablesList.get(processId) -> verificar como ter certeza
        *       q pegou pelo pid e não pelo index se ambos são int....
        *
        * para cada pagina valida dessa PageTable, settar como invalida e, tambem, liberar o bitmap referente, porem:
        *
        * IMPORTANTE: só desalocar do bitmap os trechos que esse processo nao compartilha com outro processo
        *
        *
        * */

        this.numberActiveProcess -= 1;
        pageTablesList.remove(processId);
        processList.remove(processId);
    }

    @Override
    public void resetMemory() {
        for(int pid : pageTablesList.keySet()){
            excludeProcessFromMemory(pid);
        }
        this.numberActiveProcess = 0;
        this.numFreeFrames = numFrames;
    }

    @Override
    public int getPhysicalAddress(int processId, int logicalAddress) {
        int pageNumber;
        PageTable pageTable;
        int frameNumber; //nro do quadro correspondente a uma certa pagina
        int d; //deslocamento
        int physicalAddress;
        int pageSize; //32 bytes

        pageSize = this.pageSize;
        pageNumber = logicalAddress/pageSize; //retorna a parte inteira da divisao
        pageTable = pageTablesList.get(processId); //tabela de pagina desse processo
        frameNumber = pageTable.getFrameNumber(pageNumber);
        d = logicalAddress%pageSize; //deslocamento
        physicalAddress = frameNumber*pageSize + d;

        return physicalAddress;
    }

    @Override
    public String getBitMap() {

        return null;
    }

    /**
     * Retorna cada uma das linhas da PageTable do processo no formato: pageNumber;isValid;frameNumber, ex:
     * 0;1;4 //pagina 0 é válida e corresponde ao frame 1
     * 1;0;5 //pagina 1 é inválida
     *
     **/
    @Override
    public String getPageTable(int processId) {
        String sPageTable;
        PageTable pageTable;
        int numberOfPages;

        sPageTable = "";
        pageTable= pageTablesList.get(processId);
        numberOfPages = pageTable.getNumberOfPages();

        for(int pageNumber = 0; pageNumber < numberOfPages; pageNumber++){
            sPageTable += pageNumber + ";" + pageTable.isValid(pageNumber) + ";" + pageTable.getFrameNumber(pageNumber) + "\n";
        }

        return sPageTable;
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
