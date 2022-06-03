package menu;


import game.*;
import resources.LoadSave;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static resources.LoadSave.*;

//Диалоговое окно Newgame
public class Newgame extends JDialog implements ActionListener{
    private JPanel ng_pan;
    private static JPanel bombfield;
    private JRadioButton easy, medium, hard;
    private JButton rungame;
    private JRadioButton custom;
    private JSlider js_height;
    private JSlider js_bombs;
    private JSlider js_width;
    private JTextField tf_height;
    private JLabel jl_height;
    private JLabel jl_width;
    private JTextField tf_width;
    private JTextField tf_bombs;
    private JLabel jl_max_bombs;
    private static JLabel troll;
    private static JFormattedTextField score;
    private static JFormattedTextField time;
    private static JFrame jf;
    private static int game_mode;

    public Newgame(JFrame jf, JPanel bombfield, JFormattedTextField time, JFormattedTextField score, JLabel troll){
        super(jf, "Newgame", true);
        Newgame.bombfield = bombfield;
        Newgame.time = time;
        Newgame.score = score;
        Newgame.troll = troll;
        Newgame.jf = jf;
        jl_height.setText("\u21d5");
        jl_width.setText("\u21d4");
        //При старте приложения
        createGUI();                //Создать интерфейс Newgame
        setMode();                  //Установка параметров игры
        createField();              //Наполнение минного поля bombfield
    }

    //При открытии окна Newgame
    public void actionPerformed(ActionEvent ae){
        Activation.stopGame = true;                                 //Останановить текущюю игру
        setVisible(true);


    }
    //Установка параметров в зависимости от режима: Easy, Medium....
    private void setMode(){
        switch(game_mode){
            case 1:{
                LoadSave.putParam("&nnn", 9);
                LoadSave.putParam("&mmm", 9);
                LoadSave.putParam("&bmb", 9);
                break;
            }
            case 2:{
                LoadSave.putParam("&nnn", 16);
                LoadSave.putParam("&mmm", 16);
                LoadSave.putParam("&bmb", 40);
                break;
            }
            case 3:{
                LoadSave.putParam("&nnn", 20);
                LoadSave.putParam("&mmm", 30);
                LoadSave.putParam("&bmb", 99);
                break;
            }
            //Custom game
            case 4:{

                    LoadSave.putParam("&mmm", js_width.getValue());
                    LoadSave.putParam("&nnn", js_height.getValue());
                    LoadSave.putParam("&bmb", js_bombs.getValue());

                break;
            }
        }
        LoadSave.putParam("&gmd", game_mode);
    }
    //Создание минного поля
    public static void createField(){
        Activation.stopGame = false;
        bombfield.removeAll();                                                            //Зачистить поле
        //Размеры поля
        int n = LoadSave.getParam("&nnn");
        int m = LoadSave.getParam("&mmm");
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        bombfield.setLayout(gbl);                                                        //Менеджер компоновки
        Color btn_color = new Color(getParam("&btn"));                               //Цвет кнопок
        JButton button;
        new BombsArray();                                                                //Генератор значений поля

        score.setText(Integer.toString(LoadSave.getParam("&bmb")));
        time.setText("00:00");
        troll.setIcon(new ImageIcon("D:/JavaProjects/JavaProjects/JavaProjects/src/resources/images/troll_right.png"));
        Activation act = new Activation(bombfield, score, time, troll, jf);   //Слушатель событий кнопок
        //Отрисовка кнопок
        int start = 0;
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                button = new JButton();
                button.setBackground(btn_color);
                button.setPreferredSize(new Dimension(40, 40));
                button.setMinimumSize(new Dimension(30, 30));
                gbc.gridx = j;
                gbc.gridy = i;
                gbl.setConstraints(button, gbc);
                bombfield.add(button, start);       //Индекс кнопки
                button.addMouseListener(act);
                start++;
            }
        }
        //Подгонка размеров фрейма под n*m
        jf.setMinimumSize(new Dimension(m*30+100, n*30+200));
        bombfield.validate();
        bombfield.repaint();
    }


    //Настройки режима Custom доступны при активации данного режима
    private void setCustomDisabled(boolean bool){
        if(bool){
            js_height.setEnabled(true);
            js_width.setEnabled(true);
            js_bombs.setEnabled(true);
            tf_height.setEnabled(true);
            tf_height.setEnabled(true);
            tf_width.setEnabled(true);
            tf_bombs.setEnabled(true);
        }else{
            js_height.setEnabled(false);
            js_width.setEnabled(false);
            js_bombs.setEnabled(false);
            tf_height.setEnabled(false);
            tf_height.setEnabled(false);
            tf_width.setEnabled(false);
            tf_bombs.setEnabled(false);
        }
    }
    //Интерфейс New Game
    private void createGUI(){

        setContentPane(ng_pan);
        setUndecorated(true);
        setSize(500, 500);
        setResizable(false);
        setLocationRelativeTo(jf);
        pack();

        game_mode = LoadSave.getParam("&gmd");      //Режим игры


        //При открытии окна New Game установить выбранный режим
        switch(game_mode){
            case 1:
                easy.setSelected(true);
                break;
            case 2:
                medium.setSelected(true);
                break;
            case 3:
                hard.setSelected(true);
                break;
            case 4:
                custom.setSelected(true);
                setCustomDisabled(true);
                break;
            default:
                easy.setSelected(true); //Если параметр game_mode вне диапазона целых чисел 1-4
        }
        rungame.addActionListener((ae) ->{
        setMode();
        createField();
        dispose();
        });
        //Radio buttons
        easy.addActionListener((ae) -> {game_mode = 1; setCustomDisabled(false);});
        medium.addActionListener((ae) -> {game_mode = 2; setCustomDisabled(false);});
        hard.addActionListener((ae) -> {game_mode = 3; setCustomDisabled(false);});
        custom.addActionListener((ae) -> {game_mode = 4; setCustomDisabled(true);}); //Активируем режим Custom

        //Слайдеры
        js_height.addChangeListener((ce) ->{

            setField(tf_height, js_height);
        });
        js_width.addChangeListener((ce) ->{
            setField(tf_width, js_width);
        });
        js_bombs.addChangeListener((ce) ->{
            tf_bombs.setText(String.valueOf(js_bombs.getValue()));
        });
        //Текстовы поля. Форматирование вывода
        tf_height.setDocument(new IntDocument(2));
        tf_width.setDocument(new IntDocument(2));
        tf_bombs.setDocument(new IntDocument(3));
        //При клике в поле выделить значение
        tf_height.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                tf_height.selectAll();
            }
        });
        tf_width.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                tf_width.selectAll();
            }
        });
        tf_bombs.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                tf_bombs.selectAll();
            }
        });

        tf_height.addActionListener((ae) ->{
            setSlider(tf_height, js_height);
        });
        tf_width.addActionListener((ae) ->{
            setSlider(tf_width, js_width);
        });
        tf_bombs.addActionListener((ae) ->{
            setSlider(tf_bombs, js_bombs);
        });
        //При открытии окна Newgame
        js_height.setValue(LoadSave.getParam("&nnn"));
        js_width.setValue(LoadSave.getParam("&mmm"));
        js_bombs.setMaximum((int)((js_height.getValue()*js_width.getValue())/2.5));
        js_bombs.setValue(LoadSave.getParam("&bmb"));

        jl_max_bombs.setText("9 - "+js_bombs.getMaximum());


        tf_height.setText(String.valueOf(js_height.getValue()));
        tf_width.setText(String.valueOf(js_width.getValue()));
        tf_bombs.setText(String.valueOf(js_bombs.getValue()));





    }
    //Установка значения слайдера
    private void setSlider(JTextField tf, JSlider js){
        //Если превысил максимальное или минимальное значение слайдера
        if(Integer.valueOf(tf.getText()) > js.getMaximum()) {
            js.setValue(js.getMaximum());
            tf.setText(Integer.toString(js.getMaximum()));
            return;
        }
        if(Integer.valueOf(tf.getText()) < js.getMinimum()){
            js.setValue(js.getMinimum());
            tf.setText(Integer.toString(js.getMinimum()));
        }
        js.setValue(Integer.valueOf(tf.getText()));
    }
    //Установка значения текстового поля
    private void setField(JTextField tf, JSlider js){
        tf.setText(String.valueOf(js.getValue()));
        float w = js_width.getValue();
        float h = js_height.getValue();
        float old_b = js_bombs.getValue();
        float old_max = js_bombs.getMaximum();
        //Ограничить максимальное значение мин, т.е. кол-во мин не должно превышать размер поля. Логично же!
        float new_max = ((w*h)/2.5f);
        int new_b;
        float k = (old_max-old_b)/((old_max-9)*0.01f);
        new_b = Math.round(new_max-(new_max-9)*0.01f*k);
        jl_max_bombs.setText("9 - " + Integer.toString((int)new_max));
        js_bombs.setMaximum((int)new_max);
        js_bombs.setValue(new_b);
    }
}
