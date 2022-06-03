package main;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import menu.*;
import resources.LoadSave;

import javax.swing.*;

public class MineSweeper extends JFrame {
    private JPanel bombfield;               //Минное поле
    public static Newgame new_game;
    MineSweeper(){
        super("Minesweeper");

        //Загрузка данных из файла
        new LoadSave();

        //Main frame
        setResizable(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        //Сохранить данные при выходе
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                LoadSave.saveParam();
                LoadSave.saveResults();
                System.exit(0);
            }
        });

        //Main panel
        JPanel jp_main = new JPanel();
        jp_main.setLayout(new BorderLayout());

        //Меню
        JMenuBar jmb = new JMenuBar();
        //Меню "Game"
        JMenu jGame = new JMenu("Game");
        JMenuItem jmiNew = new JMenuItem("New Game");
        JMenuItem jmiStats = new JMenuItem("Statistics");
        JMenuItem jmiOptions = new JMenuItem("Themes");
        JMenuItem jmiExit = new JMenuItem("Exit");

        jGame.add(jmiNew);
        jGame.add(jmiStats);
        jGame.add(jmiOptions);
        jGame.addSeparator();
        jGame.add(jmiExit);

        //Меню "Help"
        JMenu jHelp = new JMenu("Help");
        JMenuItem jmiRules = new JMenuItem("Rules");
        JMenuItem jmiAbout = new JMenuItem("About");

        jHelp.add(jmiRules);
        jHelp.add(jmiAbout);

        jmb.add(jGame);
        jmb.add(jHelp);
        jp_main.add("North", jmb);

        //Скелет минного поля. Наполнение в классе Newgame
        bombfield = new JPanel();
        Color bf_color = new Color(LoadSave.getParam("&bmf"));
        bombfield.setBackground(bf_color);
        jp_main.add("Center", bombfield);

        //Панель с таймером
        Font fnt = new Font("Calibri", Font.BOLD, 30);
        JPanel timer = new JPanel();
        timer.setLayout(new FlowLayout());

        JFormattedTextField score = new JFormattedTextField();      //Число мин
        JFormattedTextField time = new JFormattedTextField();       //Секундомер
        JLabel troll = new JLabel();                                //Trollface
        troll.setPreferredSize(new Dimension(36, 36));

        score.setColumns(5);
        score.setFont(fnt);
        score.setHorizontalAlignment(SwingConstants.CENTER);
        score.setEditable(false);

        time.setColumns(5);
        time.setFont(fnt);
        time.setHorizontalAlignment(SwingConstants.CENTER);
        time.setEditable(false);

        timer.add(score);
        timer.add(troll);
        timer.add(time);

        jp_main.add("South", timer);

        add(jp_main);

        //Обработчики событий
        new_game = new Newgame(this, bombfield, time, score, troll);
        jmiOptions.addActionListener((ae) -> {new Themes(this, bombfield);});   //Офомление
        jmiNew.addActionListener(new_game);                                        //Новая игра
        jmiRules.addActionListener((ae)->{new Rules(this);});                    //Правила
        jmiAbout.addActionListener((ae) ->{                                        //Об игре
            String about = "Игра \"Сапер\"\n"+
                    "Автор: Alex\n"+
                    "Версия: 1.0";
            //Диалоговое окно
            JOptionPane.showMessageDialog(this, about, "Об игре", JOptionPane.INFORMATION_MESSAGE);
        });
        //Сохранение данных при выходе
        jmiExit.addActionListener((ae) -> {                                         //Выход
            LoadSave.saveParam();
            LoadSave.saveResults();
            System.exit(0);
        });
        jmiStats.addActionListener((ae)->{new Statistics(this);});                //Статистика
        //Отобразить main frame
        setLocationRelativeTo(null);
        setVisible(true);
    }
    //Запуск приложения
    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            new MineSweeper();
        });
    }


}









