import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.util.ArrayList;
import java.util.List;


public class Lab6 extends JFrame {

    private static List<Character> history = new ArrayList<>(); // накопленные сигналы
    private static List<Character> buffer = new ArrayList<>(); // буфер
    private static JTextArea area;
    private static JScrollPane scroll;

    Lab6() {
        setTitle("Interruption");
        setSize(800, 300);
        setLocation(400, 450);

        area = new JTextArea(10, 20);
        area.setLineWrap(false);
        area.getDocument().addDocumentListener(new MyDocumentListener());
        scroll = new JScrollPane(area);

        add(scroll);
        setVisible(true);
    }


    class MyDocumentListener implements DocumentListener {
        String newline = "\n";

        // слушатель изменения текста
        @Override
        public void insertUpdate(DocumentEvent e) {
            try {
                Document doc = (Document) e.getDocument();
                int changeLength = e.getLength();
                int offset = e.getOffset();

                // Просмотр нового текста
                String newText = doc.getText(offset, changeLength);

                // Удаление текста выведеного программой а не пользователем
                newText = newText.replaceAll("Буфер:.*]", "");
                newText = newText.replaceAll("Все накопленные сигналы:.*]", "");
                newText = newText.replaceAll("Буфер отчищен.", "");
                newText = newText.replaceAll("Всего введено символов: \\d+", "");
                newText = newText.replaceAll("\n", "");

                // добавление символов в буфер
                for (char c  : newText.toCharArray())
                    if (Character.isLetterOrDigit(c))
                        buffer.add(c);
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }
        }
        @Override
        public void removeUpdate(DocumentEvent e) {}

        @Override
        public void changedUpdate(DocumentEvent e) {}
    }

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new Lab6();
        int count = 0;
        while (true) {

            if (!buffer.isEmpty()) {
                area.append("\n" + "Буфер:" + buffer + "\n"); // вывод буфера каждые 4 секунды
                area.setCaretPosition(area.getText().length());

            }
            Thread.sleep(4000);
            if (++count  %  3 == 0) { // раз в 12 секунд отчищается буфер и выводится информация о накопленных сигналах
                history.addAll(buffer);
                area.append("\n" + "Буфер:" + buffer);
                buffer.clear();
                area.append("\nБуфер отчищен.\n");
                area.append("Все накопленные сигналы: " + history + "\n");
                area.append("Всего введено символов: " + history.size() + "\n");
                area.setCaretPosition(area.getText().length()-1);
            }
        }
    }
}