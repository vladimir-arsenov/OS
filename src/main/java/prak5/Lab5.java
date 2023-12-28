package prak5;

import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Lab5 {

    public static void main(String[] args) throws Exception {

        String path = "C:\\Users\\William\\IdeaProjects\\OS\\src\\prak5\\output\\output";

        // 4 - поситать количество цифр в файлах
//        String directoryPath = "src/prak5/input";
//        java.io.File folder = new java.io.File(directoryPath);
//        java.io.File[] listOfFiles = folder.listFiles();
//        int count = 0;
//        for (java.io.File file : listOfFiles) {
//            if (file.isFile()) {
//                String text = readFrom(file.getAbsolutePath());
//                for(char c : text.toCharArray())
//                    if (Character.isDigit(c)) count ++;
//            }
//        }
//        // запись в файл
//        Writer wr = new FileWriter(path);
//        wr.write(String.valueOf(count));
//        wr.close();





        System.out.println("СОЗДАНИЕ ФАЙЛА:");
        create("test", "local\\test", "Rebellious subjects, enemies to peace,\n" +
                "Profaners of this neighbour-stained steel,--\n" +
                "Will they not hear? What, ho! you men, you beasts,\n" +
                "That quench the fire of your pernicious rage\n" +
                "With purple fountains issuing from your veins,\n" +
                "On pain of torture, from those bloody hands\n" +
                "Throw your mistemper'd weapons to the ground,\n" +
                "And hear the sentence of your moved prince.\n" +
                "Three civil brawls, bred of an airy word,\n" +
                "By thee, old Capulet, and Montague,\n" +
                "Have thrice disturb'd the quiet of our streets,\n" +
                "And made Verona's ancient citizens\n" +
                "Cast by their grave beseeming ornaments,\n" +
                "To wield old partisans, in hands as old,\n" +
                "Canker'd with peace, to part your canker'd hate:\n" +
                "If ever you disturb our streets again,\n" +
                "Your lives shall pay the forfeit of the peace.\n" +
                "For this time, all the rest depart away:\n" +
                "You Capulet; shall go along with me:\n" +
                "And, Montague, come you this afternoon,\n" +
                "To know our further pleasure in this case,\n" +
                "To old Free-town, our common judgment-place.\n" +
                "Once more, on pain of death, all men depart.\n" +
                "Exeunt all but MONTAGUE, LADY MONTAGUE, and BENVOLIO\n");
        printFiles();
        System.out.println("БЛОКИ с 0 - 3 (0 - 2 ЗАНЯТЫ ФАЙЛОМ):");
        printBlocks(0, 4);
        System.out.println();

        System.out.println("СОЗДАНИЕ ФАЙЛА ИЗ ФС КОМПЬЮТЕРА:");
        createFrom(path);
        printFiles();
        System.out.println("БЛОКИ с 0 - 4 (3 ЗАНЯТ НОВЫМ ФАЙЛОМ):");
        printBlocks(0, 5);
        System.out.println();

        System.out.println("ПЕРЕИМЕНОВАНИЕ ФАЙЛА:");
        rename("t", "local\\test");
        printFiles();
        System.out.println();

        System.out.println("ЧТЕНИЕ ФАЙЛА:");
        System.out.println(read("local\\t"));
        System.out.println();

        System.out.println("ПЕРЕМЕЩЕНИЕ ФАЙЛА:");
        move("local\\t", "dir\\t");
        printFiles();
        System.out.println();

        System.out.println("КОПИРОВАНИЕ ФАЙЛА:");
        copy("dir\\t");
        printFiles();
        System.out.println();

        System.out.println("УДАЛЕНИЕ ФАЙЛА:");
        delete("dir\\t");
        printFiles();
        System.out.println("БЛОКИ ОСВОБОЖДЕНЫ:");
        printBlocks(0, 3);

        create("test1", "test1", "Hello World");
        printFiles();
        printBlocks(0, 5);
        System.out.println(read("test1"));

    }

    private static List<File> files = new ArrayList<>(); // файлы
    private static String[][] memory = new String[128][512]; // 128 блоков размером 512 байт(байт представлен строкой длинной 8)
    private static LinkedList<Integer> freeBlocks = new LinkedList<>(); // свободные блоки
    static {
        for(int i = 0; i < memory.length; i++)
            freeBlocks.add(i);
    }

    // следующий свободный блок
    private static int nextBlock() { return freeBlocks.pop(); }


    // создание файла из ФС компьютера
    public static void createFrom(String path) throws Exception{
        String s = readFrom(path);
        path = new java.io.File(path).getAbsolutePath();
        create(path.substring(path.lastIndexOf("\\")+1), path, s);
    }

    // создание файла с указанием имени файла, данных ввиде текста, пути по которому файл будет расположен
    public static void create(String fileName, String path, String data) {
        String binary = stringToBinary(data); // переводим текст файла в двоичную строку

        int size = binary.length() / 8;

        // выделение блоков для файла
        List<Integer> blocks = new ArrayList<>();
        for(int i = 0; i < size / 512; i++)
            blocks.add(nextBlock());
        if (size > size / 512) // неполный блок
            blocks.add(nextBlock());

        // запись данных о файле
        File newFile = new File(fileName,path, size, new ArrayList<Integer>(blocks));
        files.add(newFile);

        // запись файла в выделенные блоки
        writeBinaryBlock(binary, blocks);
    }


    // удаление файла
    public static boolean delete(String path) {
        for(File f : files) {
            if (f.getPath().equals(path)) {
                // отчищаем блоки
                String emptyByte = "00000000";
                for(int b : f.getBlocks()) {
                    for(int i = 0; i < 512; i++)
                        memory[b][i] = emptyByte;
                    freeBlocks.add(b);
                }

                // удаляем файл
                files.remove(f);
                return true;
            }
        }
        System.out.println("Файл не найден");
        return false;
    }

    // копирование файла и заменой его имени
    public static void copy(String path, String newName) {
        for (File f : files) {
            if (f.getPath().equals(path)) {
                List<Integer> copyBlocks = new LinkedList<>();
                for(int b : f.getBlocks()) {
                    int nextBlock = nextBlock();
                    copyBlocks.add(nextBlock);
                    memory[nextBlock] = Arrays.copyOf(memory[b], memory[b].length);
                }
                File copy = new File(newName, f.getPath().replace(f.getName(), newName), f.getSize(), copyBlocks);
                files.add(copy);
                return;
            }
        }
        System.out.println("Файл не найден");
    }

    // если не указано имя для копии то меняем имя по умолчанию: добавляем _copy
    public static void copy(String path) {
        copy(path, "_copy");
    }

    // перемещение файла на новые блоки
    public static void move(String path, String newPath, List<Integer> newLocation) {
        List<Integer> newLoc = new ArrayList<Integer>(newLocation);
        for (File f : files) {
            if (f.getPath().equals(path)) {
                for(int b : f.getBlocks()) {
                    int nextBlock = newLoc.remove(0);
                    memory[nextBlock] = Arrays.copyOf(memory[b], memory[b].length);
                }
                File newF = new File(f.getName(), newPath, f.getSize(), newLocation);
                files.add(newF);
                delete(f.getPath());
                return;
            }
        }
        System.out.println("Файл не найден");
    }

    // перемещение файла (изменение пути)
    public static void move(String path, String newPath) {
        for (File f : files) {
            if (f.getPath().equals(path)) {
                f.setPath(newPath);
                return;
            }
        }
        System.out.println("Файл не найден");
    }

    // чтение файла
    public static String read(String path) {
        for (File f : files) {
            if (f.getPath().equals(path)) {
                StringBuilder sb = new StringBuilder();
                List<Integer> blocks = new ArrayList<>(f.getBlocks());
                int curBlock = -1;
                for (int i = 0; i < f.getSize(); i++) {
                    if (i % 512 == 0) // если блок заполнен берём следующий
                        curBlock = blocks.remove(0);
                    sb.append(memory[curBlock][i%512]);
                }
                return binaryToString(sb.toString());
            }
        }
        System.out.println("Файл не найден");
        return null;
    }

    // чтение файла из ФС компьютера
    public static String readFrom(String fileName) throws IOException {
        // считывание файла в строку
        StringBuilder sb = new StringBuilder();
        String path = new java.io.File(fileName).getAbsolutePath();
        BufferedReader in = new BufferedReader(new FileReader(path));
        String s;
        while ((s = in.readLine()) != null) {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }

    // переименование файла
    public static boolean rename(String newName, String path) {
        for (File f : files) {
            if (f.getPath().equals(path)) {
                File newFile = new File(newName, f.getPath().replace(f.getName(), newName), f.getSize(), f.getBlocks());
                files.set(files.indexOf(f), newFile);
                return true;
            }
        }
        System.out.println("Файл не найден");
        return false;
    }




    // вспомогательный метод для заполнения блоков бинарной строкой
    private static void writeBinaryBlock(String binary, List<Integer> blocks) {
        // заполняем нулями оставшееся место в блоке
        String emptyByte = "00000000";
        int size = binary.length()/8;
        for(int i = size % 512; i < 512; i++)
            memory[blocks.get(blocks.size()-1)][i] = emptyByte;

        // заполнем блоки значащей информацией
        int curBlock = -1;
        for(int i = 0; i < binary.length()/8; i++) {
            if (i % 512 == 0) // если блок заполнен берём следующий
                curBlock = blocks.remove(0);
            memory[curBlock][i%512] = binary.substring(i*8, i*8+8); // записываем байт
        }
    }


    // вспомогательный метод для перевода символьной строки в бинарную
    private static String stringToBinary(String in) {
        StringBuilder result = new StringBuilder();
        for (char c : in.toCharArray()) {
            result.append(
                    String.format("%8s", Integer.toBinaryString(c)) // строка размером 8 c пробелами перед двоичным представлением символа 'c'
                            .replaceAll(" ", "0") // заменяем пробелы незначащими нулями
            );
        }
        return result.toString();
    }

    // вспомогательный метод для перевода бинарной строки в символьную
    private static String binaryToString(String binary) {
        StringBuilder result = new StringBuilder();
        char[] chars = binary.toCharArray();
        for (int i = 8; i <= binary.length(); i += 8) {
            result.append((char)Integer.parseUnsignedInt(binary.substring(i-8, i), 2));
        }
        return result.toString();
    }

    // вывод информации о блоках
    public static void printBlocks(int s, int f) {
        for(int i = s; i < f; i++) {
            System.out.println(Arrays.toString(memory[i]));
        }
    }

    // вывод информации о созданных файлах
    public static void printFiles() {
        for(File f : files) System.out.println(f);
    }

    static class File {
        private String name; // имя файла
        private String path; // путь файла, индивидуальный идентификатор файла
        private int size; // bytes
        private List<Integer> blocks; // индексы блоков на которых хранятся данные файла

        public String getPath() {
            return path;
        }
        public int getSize() {
            return size;
        }
        public String getName() {
            return name;
        }
        public List<Integer> getBlocks() {
            return blocks;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public File(String name, String path, int size, List<Integer> blocks) {
            this.name = name;
            this.path = path;
            this.size = size;
            this.blocks = blocks;
        }

        public File(int size, List<Integer> blocks) {
            this.size = size;
            this.blocks = blocks;
        }

        @Override
        public boolean equals(Object obj) {
            return this.path.equals(((File)obj).path);
        }

        @Override
        public String toString() {
            return "File{" +
                    "name='" + name + '\'' +
                    ", path='" + path + '\'' +
                    ", size=" + size +
                    ", blocks=" + blocks +
                    '}';
        }
    }

}

