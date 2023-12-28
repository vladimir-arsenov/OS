import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;
public class Lab61 {

        static boolean f1Stop = true;
        static boolean f2Stop = true;
        static boolean f3Stop = true;

        static String previousKey = "A";

        public static void main(String[] args) {
            System.out.println("Press 1, 2, 3 or e to exit");

            // Пул потоков бесконечно выводящих сообщения
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
            executor.scheduleAtFixedRate(Lab61::f1, 0, 1, TimeUnit.SECONDS);
            executor.scheduleAtFixedRate(Lab61::f2, 0, 1, TimeUnit.SECONDS);
            executor.scheduleAtFixedRate(Lab61::f3, 0, 1, TimeUnit.SECONDS);

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String key = scanner.nextLine();
                if (key.equals("e")) System.exit(0); // клавиша - признак завершения
                else if (key.equals(previousKey)) { // если нажата та же самая клавиша соответствующая функция прерывается
                    f1Stop = true;
                    f2Stop = true;
                    f3Stop = true;
                    try {
                        Thread.sleep(4000); // вывод через интервал
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (key.equals("1")) { // по нажатию клавиши запускается соответствующая функция
                    f1Stop = false;
                    f2Stop = true;
                    f3Stop = true;
                } else if (key.equals("2")) {
                    f1Stop = true;
                    f2Stop = false;
                    f3Stop = true;
                } else if (key.equals("3")) {
                    f1Stop = true;
                    f2Stop = true;
                    f3Stop = false;
                }

                previousKey = key; // сохранение предыдущего ключа
            }
        }

        // функции которые выводят текст, используются потоками
        static void f1() { if (!f1Stop) System.out.print("f1 ");}
        static void f2() { if (!f2Stop) System.out.print("f2 ");}
        static void f3() { if (!f3Stop) System.out.print("f3 ");}
}
