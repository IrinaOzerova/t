package game;

import main.MineSweeper;
import menu.Newgame;
import resources.LoadSave;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *Отображение результата текущей игры
 */
public class Result extends JDialog{
    private JPanel stats;
    private JTextField tf_fsize;
    private JTextField tf_mines;
    private JTextField tf_time;
    private JTextField tf_btime;
    private JButton ng_button;
    private JLabel jl_mode;
    private JLabel jl_condition;
    private JButton ag_button;
    private static String custom_time = ""; //хранится только во время работы приложения. Не сейвим

    public Result(JFrame jf, String time, String win_fail){
        super(jf, "Result", true);
        setContentPane(stats);
        setUndecorated(true);
        setSize(500, 500);
        setResizable(false);
        setLocationRelativeTo(jf);
        pack();
        //Оформление
        switch (LoadSave.getParam("&gmd")){
            case 1:
                jl_mode.setText("Ease mode");
                break;
            case 2:
                jl_mode.setText("Medium mode");
                break;
            case 3:
                jl_mode.setText("Hard mode");
                break;
            case 4:
                jl_mode.setText("Custom mode");
                break;
        }
        jl_condition.setText(win_fail);                                               //Проигрыш - поражение
        tf_fsize.setText(LoadSave.getParam("&nnn")+"x"+ LoadSave.getParam("&mmm"));  //Размер поля
        tf_mines.setText(Integer.toString(LoadSave.getParam("&bmb")));               //Количество мин
        tf_time.setText(time);                                                        //Текущий результат
        int gmd = LoadSave.getParam("&gmd");             //Режим игры: 1 - easy, 2 - med, 3 - hard, 4 - custom
        //
        //Формирование поля "Лучший результат"
        //В случае проигрыша выводим данные из массива
        if(win_fail.equals("Loseeeerrr")) {
            if (gmd == 4) {
                tf_btime.setText(custom_time);                      //Кастомная игра не записывается в файл
            } else {
                tf_btime.setText(LoadSave.getArray(gmd, 0));
            }
            //В случае победы сравниваем с лучшим результатом из массива
        }else {
            //Обновляем рекорд
            if (!(time).equals("00:00")) { //Не засчитывать время в 00:00 за результат
                if (gmd == 4 ){
                    if(time.compareTo(custom_time)<0 || custom_time == "") custom_time = time;
                }
                else {
                    for(int i=0; i<10; i++){
                        if(time.compareTo(LoadSave.getArray(gmd, i))<0 || LoadSave.getArray(gmd, i) == ""){
                            LoadSave.putArray(gmd, i, time);
                            break;
                        }
                    }
                }
            }tf_btime.setText(gmd == 4 ? custom_time : LoadSave.getArray(gmd, 0));

            }

        //Вывести окно Newgame
        ng_button.addActionListener((ae) ->{
            main.MineSweeper.new_game.setVisible(true);
            dispose();
        });
        //Повторить игру без изменения настроек
        ag_button.addActionListener((ae) ->{
                Newgame.createField();
                dispose();
        });
        setVisible(true);

    }





}