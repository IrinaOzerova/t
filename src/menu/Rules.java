package menu;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
//Диалоговое окно "Правила игры"
public class Rules extends JDialog {

    public Rules(JFrame jf) {
        String rules = "";
        //Чтение файла
        try (FileInputStream fin = new FileInputStream("D:/JavaProjects/JavaProjects/JavaProjects/src/resources/rules.txt")) {
            InputStreamReader isr = new InputStreamReader(fin, "UTF-8");
            int i;
            while ((i = isr.read()) != -1) {
                rules += Character.toString((char) i);
            }
            JPanel contentPane = new JPanel();
            JTextArea text = new JTextArea(rules, 20, 30);
            text.setLineWrap(true);                                        //переносить на новую строку
            text.setWrapStyleWord(true);                                   //переносить слова на новую строку
            text.setEditable(false);
            contentPane.add(new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));                   //только вертикальная полоса прокрутки
            JDialog jd = new JDialog(jf, "Rules", true);
            jd.setContentPane(contentPane);
            jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            jd.setSize(400, 400);
            jd.setLocationRelativeTo(jf);
            jd.pack();
            jd.setVisible(true);
        } catch (IOException io) {
            JOptionPane.showMessageDialog(Rules.this,
                    "Файл не найден.\nПосмотрите правила на wiki");
        }
    }
}

