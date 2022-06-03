package game;



import java.awt.event.MouseAdapter;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
//Секундомер
public class Timer extends MouseAdapter implements Runnable{
    private JFormattedTextField time;

    private JLabel trollwatch;
    private ImageIcon t_left, t_right, t_sad;
    public Timer (JFormattedTextField time, JLabel troll){
        this.time = time;
        this.t_left = new ImageIcon("D:/JavaProjects/JavaProjects/JavaProjects/src/resources/images/troll_left.png");  //Взгляд влево
        this.t_right = new ImageIcon("D:/JavaProjects/JavaProjects/JavaProjects/src/resources/images/troll_right.png");   //Взгляд вправо
        this.t_sad = new ImageIcon("D:/JavaProjects/JavaProjects/JavaProjects/src/resources/images/troll_sad.png");       //Trollsad
        this.trollwatch = troll;
        Thread t = new Thread(this, "ScoreTime");
        t.start();
    }
    public void run(){
        try{
            int i = 0;
            while (!Activation.stopGame){
                time.setText(String.format("%02d:%02d", i / 60, i % 60));  // Format: 00:00
                i++;
                //Генерация случайного значения
                int r = new Random().nextInt(2);

                if(r == 1){
                    int r2 = new Random().nextInt(3);
                    switch(r2){
                        case 0: {
                            trollwatch.setIcon(t_left);
                            break;
                        }
                        case 1: {
                            trollwatch.setIcon(t_right);
                            break;
                        }
                        case 2: {
                            trollwatch.setIcon(t_sad);
                            break;
                        }
                    }
                }
                Thread.sleep(1000);
            }


        }catch(InterruptedException ie){
            System.out.println("Поток прерван:"+" "+ie);
        }
    }
}

