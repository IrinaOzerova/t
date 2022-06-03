package menu;

import resources.LoadSave;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

//Статистика
public class Statistics extends JDialog{
    private JButton jb_easy;
    private JPanel jp_stats;
    private JPanel jp_mode;
    private JButton jb_medium;
    private JButton jb_hard;
    private JTable jt_easy;
    private JTable jt_medium;
    private JTable jt_hard;

    public Statistics(JFrame jf){
        super(jf, "Statistics", true);
        setContentPane(jp_stats);
        setSize(500, 500);
        setResizable(false);
        setLocationRelativeTo(jf);
        pack();

        Color clr_show = new Color(0x1B413B);           //Активная панель
        Color clr_hide = new Color(0x6CAABB);           //Неактивная панель
        //Таблицы результатов (ТОП 10)
        jt_easy.setModel(new MyTableModel(1));
        jt_medium.setModel(new MyTableModel(2));
        jt_hard.setModel(new MyTableModel(3));

        //Обработчики событий
        jb_easy.addActionListener((ae)-> {
                CardLayout layout = (CardLayout)(jp_mode.getLayout());
                layout.show(jp_mode, "easy");
                jb_easy.setBackground(clr_show);
                jb_medium.setBackground(clr_hide);
                jb_hard.setBackground(clr_hide);
        });
        jb_medium.addActionListener((ae)->{
            CardLayout layout = (CardLayout)(jp_mode.getLayout());
            layout.show(jp_mode, "medium");
            jb_easy.setBackground(clr_hide);
            jb_medium.setBackground(clr_show);
            jb_hard.setBackground(clr_hide);
        });
        jb_hard.addActionListener((ae) -> {
            CardLayout layout = (CardLayout)(jp_mode.getLayout());
            layout.show(jp_mode, "hard");
            jb_easy.setBackground(clr_hide);
            jb_medium.setBackground(clr_hide);
            jb_hard.setBackground(clr_show);
        });
        setVisible(true);

    }
    //Пользовательская таблица результатов
    class MyTableModel extends AbstractTableModel {
        int gm;
        MyTableModel(int gm){
            this.gm = gm;
        }
        //10 строк
        public int getRowCount() {
            return 10;
        }

        //2 колонки
        public int getColumnCount() {
            return 2;
        }
        //шапка
        public String getColumnName(int c) {
            switch (c) {
                case 0:
                    return "Position";
                case 1:
                    return "Time";
            }
            return null;
        }

        //Заполнение таблицы
        public Object getValueAt(int r, int c) {
            switch (c) {
                case 0:
                    return r + 1;
                case 1:
                    return LoadSave.getArray(gm, r);
            }
            return null;
        }

    }
}
