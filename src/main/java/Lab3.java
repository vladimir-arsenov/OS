import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Lab3 {
    public static CyclicBarrier b;
    public static int stage = 0; // номер текущей точки
    public static Map<String, T> threads = new HashMap<>(); // для обращения к другим потокам

    public static void main(String[] args) {
        Resource r = new Resource();
        b = new CyclicBarrier(1);
        T t = new T("A");
        t.start();
        t.setRes(r);
    }
}

class T extends Thread {
    private int n = -1;
    private Resource resource;

    public T(String name) {
        super(name);
    }

    @Override
    public void run() {
        try {
            if (getName().equals("K")) { // заверщающие действия при достижение последнего потока
                System.out.println("Поток К начался.");
                System.out.println("The End");
                System.exit(0);
            }

            System.out.println("Поток '" + getName() + "' начался и ожидает семафор.");
            while (resource == null) {} // поток ждёт ресурс (семафор)
            System.out.print("");
            Lab3.b.await(); // ждём пока все потоки получат ресурс (с семафором)

            System.out.println("Поток '" + getName() + "' захватывает семафор.");
            Lab3.b.await(); // ждём пока все потоки приготовятся захватить семафор

            n = resource.takeAndRelease(getName()); // поток захватывает семафор и выходит из него
            System.out.println("Переменная семафора равна: " + n + ". Поток '" + getName() + "' выходит из семафора.");
            Lab3.b.await(); // ждём пока все потоки выйдут из семафора (достигнут следующей точки)

            if (n == 0) { // если это первый поток достигший семафора он должен породить потоки
                System.out.println();
                if (Lab3.stage == 0) {
                    addNewThreads("C", "B", "I", "J");
                    setResourceToThreads(resource, "C", "B");
                    Lab3.b = new CyclicBarrier(2);
                } else if (Lab3.stage == 1) {
                    addNewThreads("D", "E", "F");
                    setResourceToThreads(resource, "D", "E", "F", "J");
                    Lab3.b = new CyclicBarrier(4);
                } else if (Lab3.stage == 2) {
                    addNewThreads("G", "H");
                    setResourceToThreads(resource, "G", "H", "I");
                    Lab3.b = new CyclicBarrier(3);
                } else if (Lab3.stage == 3) {
                    addNewThreads("K");
                }
                resource.reset(); // переустанавливаем переменную семафора
                Lab3.stage++; // помечаем что началась следующая стадия
            }
        } catch (Exception e) {
            System.out.println("Broken");
        }
    }

    public void setRes(Resource resource) {
        this.resource = resource;
    }

    private void setResourceToThreads(Resource resource, String... names) {
        for(String name : names) {
            Lab3.threads.get(name).setRes(resource);
        }
    }

    private void addNewThreads(String... names) {
        for(String name : names) {
            T t = new T(name);
            t.start();
            Lab3.threads.put(name, t);
        }
    }

}

// класс в который обарачивается семафор, чтобы хранить переменную семафора
class Resource {
    private Semaphore semaphore = new Semaphore(1, true);
    private int x = 0;

    public int takeAndRelease(String name) throws InterruptedException {
        semaphore.acquire(); // захват семафора при вызове из потока
        System.out.println("Поток '" + name + "' в семафоре.");
        semaphore.release();
        return x++;
    }

    public void reset() { x = 0; }
}