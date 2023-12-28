import java.util.*;

public class Lab4 {

    public static void main(String[] args) {
        addProcess();
        addProcess();
        addProcess();
        print();

        addLine("Процесс 0", "p0l1");
        addLine("Процесс 0", "p0l2");
        int n = 4;
        for(int i = 0; i < n; i++)
            addLine("Процесс 1", "p1l" + (i+1));
        n = 7;
        for(int i = 0; i < n; i++)
            addLine("Процесс 2", "p2l" + (i+1));

        print();
        addLine("Процесс 2", "p2l8" );
        print();

        Scanner scan = new Scanner(System.in);
        System.out.print("Введите назание процесса: ");
        String name = scan.nextLine();
        System.out.print("Введите номер строки процесса: ");
        int lineNumber = scan.nextInt();
        removeLine(name, lineNumber);
        print();

        scan.nextLine();
        System.out.print("Введите назание процесса: ");
        name = scan.nextLine();
        System.out.println();
        System.out.print("Введите номер строки процесса: ");
        lineNumber = scan.nextInt();
        removeLine(name, lineNumber);
        print();
    }

    private static List<List<String[]>> pr = new ArrayList<>();
    private static String[][] cpu = new String[16][2];

    private static String[][] hdd = new String[10][2];

    static {
        for(String[] i : hdd) {
            i[0] = "";
            i[1] = "";
        }
        for(String[] i : cpu) {
            i[0] = "";
            i[1] = "";
        }
    }

    private static final List<Integer> freeLines = new ArrayList<>(16);
    static { for(int i = 0; i < 16; i++) freeLines.add(i);}
    private static int nextHdd = 0;
    private static final Random rand = new Random();

    // 1
    public static void addProcess() {
        String[] zeroLine = new String[3];
        if (freeLines.isEmpty()) // если ФП заполнена
            zeroLine[1] = swapRandomLine();
        else
            zeroLine[1] = String.valueOf(freeLines.remove(rand.nextInt(freeLines.size())));
        zeroLine[0] = "ФП";
        zeroLine[2] = "Процесс " + pr.size();

        List<String[]> newProcess = new ArrayList<>();
        newProcess.add(zeroLine);
        pr.add(newProcess);

        cpu[Integer.parseInt(zeroLine[1])] = new String[] {zeroLine[2], String.valueOf(0)};
    }

    // 2
    public static void addLine(String processName, String data) {
        String[] newLine = new String[3];
        if (freeLines.isEmpty()) // если ФП заполнена
            newLine[1] = swapRandomLine();
        else
            newLine[1] = String.valueOf(freeLines.remove(rand.nextInt(freeLines.size())));
        newLine[0] = "ФП";
        newLine[2] = data;


        for(List<String[]> p : pr) {
            if (p.get(0)[2].equals(processName)) {
                cpu[Integer.parseInt(newLine[1])] = new String[] { processName, String.valueOf(p.size())};
                p.add(newLine);
                break;
            }
        }
    }

    // 3
    public static void removeLine(String processName, int lineNumber) {
        // удаление строки из ФП
        int lineInMemory = -1;
        boolean isInCpu = false;
        for(int i = 0; i < cpu.length; i++) {
            if (cpu[i][0].equals(processName) && Integer.parseInt(cpu[i][1]) == lineNumber) {
                cpu[i] = new String[] {"", ""};
                freeLines.add(i); // появляется свободная строка в ФП
                lineInMemory = i;
                isInCpu = true;
                break;
            }
        }
        if(!isInCpu) {
            for(int i = 0; i < hdd.length; i++) {
                if (hdd[i][0].equals(processName) && Integer.parseInt(hdd[i][1]) == lineNumber) {
                    String[] toBeRemoved = hdd[i];
                    hdd[i] = new String[] {"", ""};
                    // перенос строки в ФП и её удаление
                    nextHdd--;
                    int n;
                    if (freeLines.size() > 0) {
                        n = freeLines.remove(rand.nextInt(freeLines.size()));
                    } else {
                        n = Integer.parseInt(swapRandomLine());
                        freeLines.add(n); // появляется свободная строка в ФП
                    }
                    cpu[n] = toBeRemoved;
                    cpu[n] = new String[] {"", ""};
                    lineInMemory = i;
                    break;
                }
            }
        }


        // удаление строки из процесса
        for (int i = 0; i < pr.size(); i++) { // ????????????????????????
            List<String[]> p = pr.get(i);
            if (p.get(0)[2].equals(processName)) {
                for(int j = 0; j < p.size(); j++) {
                    if (Integer.parseInt(p.get(j)[1]) == lineInMemory && (isInCpu && p.get(j)[0].equals("ФП") || !isInCpu && p.get(j)[0].equals("ВП"))) {
                        pr.get(i).remove(j);
                        break;
                    }
                }
                break;
            }
        }
    }

    public static void print() {
        System.out.println("Процессы:");
        for (List<String[]> p : pr) {
            for (String[] line : p)
                System.out.println(line[0] + " - " + line[1] + " - " + line[2]);
        }
        System.out.println("-----------------------");
        System.out.println("ФП:");
        for (String[] line : cpu) {
            System.out.println(line[0] + " - " + line[1]);
        }
        System.out.println("-----------------------");
        System.out.println("ВП:");
        for (String[] line : hdd) {
            System.out.println(line[0] + " - " + line[1]);
        }
        System.out.println("-----------------------");
    }



    private static String swapRandomLine() {
        int n = rand.nextInt(16);

        // переносим строку из ФП в ВП
        String[] lineToTransfer = cpu[n];
        cpu[n] = new String[] {"", ""};
        hdd[nextHdd++] = lineToTransfer;

        // меняем запись с ФП на ВП и номер строки в процессе, который переносим
        String processName = lineToTransfer[0];

        for (List<String[]> p : pr) {
            if (p.get(0)[2].equals(processName)) {
                for(int j = 0; j < p.size(); j++) {
                    if(Integer.parseInt(p.get(j)[1]) == n) {
                        p.get(j)[0] = "ВП";
                        p.get(j)[1] = String.valueOf(nextHdd-1);
                    }
                }
                break;
            }
        }

        return String.valueOf(n); // возвращаем номер освободившейся строки
    }
}
