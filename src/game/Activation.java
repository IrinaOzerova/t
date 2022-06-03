package game;


import resources.LoadSave;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.Border;
//Обработчик событий кнопок
public class Activation extends MouseAdapter{
    private JPanel bombfield;
    private GridBagLayout gbl;
    private GridBagConstraints gbc;
    private ImageIcon boom, may_be, q;
    private boolean flag[];
    public static boolean stopGame;
    private int n, m, max_bombs, total;
    private int position[][];
    private Font f;
    private Border brd;
    private JFormattedTextField score;
    private JFormattedTextField time;
    private JLabel troll;
    private JFrame jf;
    private boolean runtimer;
    private Map<Integer, Integer> data_array;

    public Activation(JPanel bombfield, JFormattedTextField score, JFormattedTextField time, JLabel troll, JFrame jf){
        this.bombfield = bombfield;
        this. gbl = (GridBagLayout)bombfield.getLayout();
        this.gbc = new GridBagConstraints();
        this.n = LoadSave.getParam("&nnn");
        this.m = LoadSave.getParam("&mmm");
        this.max_bombs = LoadSave.getParam("&bmb");
        this.flag = new boolean[n*m];                                               //Флаг отметки ячейки с миной
        this.total = (n*m) - max_bombs;                                             //Требуется открыть для победы
        this.score = score;
        this.time = time;
        this.troll = troll;
        this.jf = jf;
        this.runtimer = false;
        this.brd = BorderFactory.createLineBorder(Color.RED, 1, true);
        this.f = new Font("Calibri", Font.BOLD, 30);
        this.boom = new ImageIcon("D:/JavaProjects/JavaProjects/JavaProjects/src/resources/images/bomb.png");         //мина
        this.may_be = new ImageIcon("D:/JavaProjects/JavaProjects/JavaProjects/src/resources/images/flag.png");       //флаг
        this.q = new ImageIcon("D:/JavaProjects/JavaProjects/JavaProjects/src/resources/images/what.png");            //ячейка под вопросом
        this.position = new int[m][n];                                              //матрица позиции ячейки
        data_array = new HashMap<>();
        data_array.putAll(BombsArray.getMap());                                     //Скопоровать массив данных
        int index = 0;
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                position[j][i] = index;                                             //заполнение матрицы
                index++;
            }
        }
    }

    public void mousePressed(MouseEvent me){
        if(stopGame) return;                                                        //игра остановлена
        if(!runtimer) runTimer();                                                   //запуск секундомера
        runtimer = true;

        //Левая кнопка мыши
        if(me.getButton() == MouseEvent.BUTTON1){
            int start = bombfield.getComponentZOrder(me.getComponent());            //Взять индекс кнопки
            JButton b = (JButton)me.getComponent();
            if(b.getIcon() != null & !flag[start]) return;                //Не открывать если ячейка отмечена флагом
            //Вычисление координаты ячейки
            int y = (int)start/m;
            int x = start - (y*m);
            gbc.fill = GridBagConstraints.BOTH;

            switch(data_array.get(start)){
                case -1: {                          //мина
                    stopGame = true;
                    gameOver(start, x, y);          //Отображение мины
                    new MyRunnable();               //Поток отображения мин на поле


                    break;
                }
                case 0:{                            //пусто
                    setNull(start, x, y);           //Отображение пусто
                    bombfield.validate();
                    bombfield.repaint();
                    break;
                }
                default:{                           //число
                    setNumber(start, x, y);         //Отображение числа
                    bombfield.validate();
                    bombfield.repaint();

                    break;
                }
            }
            //Условие победы
            if(total == 0) {
                stopGame = true;                    //Остановить игру
                showStatistics("Winner!");
            }




        }
        //Правый клик мыши
        if(me.getButton() == MouseEvent.BUTTON3){
            JButton b = (JButton)me.getComponent();
            int x = bombfield.getComponentZOrder(me.getComponent());    //Взять индекс кнопки
            setFlag(b, x);                                              //Отобразить флаг, знак вопроса...
        }
    }
    //Отрисовка мин
    private void gameOver(int pos, int x, int y){
        bombfield.remove(pos);
        JLabel l = setLabel(-1, x, y);                          //Формирование метки
        bombfield.add(l, pos);

    }


    //Отображение числовых значений
    private void setNumber(int pos, int x, int y){
        bombfield.remove(pos);
        JLabel l = setLabel(data_array.get(pos), x, y);                //Генерация метки
        bombfield.add(l, pos);
        data_array.remove(pos);                                        //Удалить значение из массива данных
        total--;                            //ближе к победе!
    }
    //Отображение пустой ячейки
    private void setNull(int pos, int x, int y){
        bombfield.remove(pos);
        JLabel l = setLabel(0, x, y);                           //Формирование метки
        bombfield.add(l, pos);
        total--;
        data_array.remove(pos);                                        //Удалить значение из массива данных
        //Проверка смежных ячеек на "пусто"
        for(int k=-1; k<2; k++){
            for(int t=-1; t<2; t++) {
                try {
                    int new_pos = position[x + t][y + k];              //Установить индекс смежной ячейки
                    if (data_array.get(new_pos) == null){ //значение было удалено... пропуск
                        continue;}
                    if (data_array.get(new_pos) > 0) setNumber(new_pos, x + t, y + k); //Установить число
                    else if (data_array.get(new_pos) == 0) setNull(new_pos, x + t, y + k); //Рекурсия, если пусто
                } catch (ArrayIndexOutOfBoundsException e) {
                    /*Пропуск несуществующего индекса*/
                }
            }
        }
    }


    //Отображение флага или вопроса
    private void setFlag(JButton b, int x){
        /*Если кнопка не имеет иконки, то ставим флаг
        * Если кнопка иммет иконку, но flag = false, то ставим вопрос
        * Если флаг включен и кнопка имеет иконку, то в исходное состояние*/
        if(b.getIcon() == null){
            b.setIcon(may_be);
            score.setText(Integer.toString(--max_bombs)); //понизить значение числа мин
        }
        else if(!flag[x]){
            b.setIcon(q);
            flag[x] = true;
            score.setText(Integer.toString(++max_bombs)); //повысить значение числа мин
        }
        else{
            b.setIcon(null);
            flag[x] = false;
        }

    }
    //Палитра чисел
    private Color setColoredNumber(int num){
        Color clr;
        switch(num){
            case 1: {
                clr = new Color(63, 72, 204);
                break;
            }
            case 2: {
                clr = new Color(0, 128, 0);
                break;
            }
            case 3: {
                clr = new Color(202, 0, 20);
                break;
            }
            case 4: {
                clr = new Color(139, 63, 75);
                break;
            }
            case 5: {
                clr = new Color(247, 116, 72);
                break;
            }
            default: {
                clr = new Color(11, 111, 111);
                break;
            }
        }
        return clr;
    }
    //Формирование метки(замена кнопки)
    private JLabel setLabel(int number, int x, int y){
        JLabel jl = new JLabel();
        //Число
        if(number>0){
            jl.setText(Integer.toString(number));
            jl.setFont(f);
            jl.setForeground(setColoredNumber(number));
        //Мина
        }else if(number == -1){
            jl.setIcon(boom);
        }

        jl.setBackground(new Color(LoadSave.getParam("&lbn")));
        jl.setHorizontalAlignment(SwingConstants.CENTER);
        jl.setPreferredSize(new Dimension(40, 40));
        jl.setMinimumSize(new Dimension(30, 30));
        jl.setBorder(brd);
        jl.setOpaque(true);
        gbc.gridx = x;
        gbc.gridy = y;
        gbl.setConstraints(jl, gbc);
        return jl;
    }

    private void runTimer(){
        new Timer(time, troll);
    }               //секундомер

    //Отображение статистики текущей игры
    private void showStatistics(String win_fail){
        new Result(jf, time.getText(), win_fail);
    }

    //Поток отображения мин на поле
    class MyRunnable  implements Runnable{
        private int delay;          //задержка
        MyRunnable(){
            Thread t = new Thread(this, "Release");
            t.start();
        }
        public void run(){
            try{
                jf.setEnabled(false);                                           //временная блокировка фрейма
                delay = (LoadSave.getParam("&bmb") <60 ? 100 : 15);            //уменьшить задержку, если много мин
                for(Map.Entry<Integer, Integer> itr : data_array.entrySet()){
                    if(itr.getValue() == -1) {                                  //Мина
                        int y = (int)itr.getKey()/m;
                        int x = itr.getKey() - (y*m);
                        gameOver(itr.getKey(), x, y);
                        bombfield.validate();
                        bombfield.repaint();
                        Thread.sleep(delay);
                    }
                }
                jf.setEnabled(true);
                showStatistics("Loseeeerrr");
            }catch(InterruptedException ie){
                jf.setEnabled(true);
                System.out.println("Поток отображения прерван: " +ie);
            }
        }
    }
}

