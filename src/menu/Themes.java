package menu;

import resources.LoadSave;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

//Оформление минного поля
public class Themes extends JDialog{
    private JPanel m_pan;
    private JButton editButton;
    private JButton OKButton;
    private JButton cancelButton;
    private JPanel light_pan;
    private JPanel dark_pan;
    private JPanel custom_pan;
    private JLabel jl_one, jl_two, jl_bomb, jl_button;
    private JPanel jp_field;
    private JPanel bombsfield;
    private int changer; // выбранная тема LoadSave.param

    public  Themes(JFrame jf, JPanel bmbfld) {
        super(jf, "Themes", true);
        this.bombsfield = bmbfld;
        setContentPane(m_pan);
        setUndecorated(true);
        setSize(500, 500);
        setResizable(false);
        setLocationRelativeTo(jf);
        pack();
        Border bf = BorderFactory.createLineBorder(Color.red);
        jl_one.setBorder(bf);
        jl_two.setBorder(bf);
        jl_bomb.setBorder(bf);
        jl_button.setBorder(bf);
        changer = LoadSave.getParam("&clm"); //установка цветовой схемы из сейва
        setSelected(changer);                 //обозначить текущюю тему

        //Обработчики событий
        light_pan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                changer = 1;
                setSelected(changer);
            }
        });
        dark_pan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                changer = 2;
                setSelected(changer);
            }
        });
        custom_pan.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    changer = 3;
                    setSelected(3);

            }
        });
        cancelButton.addActionListener( (ae) ->{
            dispose();
        });
        //Кастомайзер пользовательской темы
        editButton.addActionListener((ae)->{
            Color color_model[] = CustomTheme.showDialog(this, jl_one.getBackground(),
                    jl_button.getBackground(),
                    jp_field.getBackground());
            if(color_model == null) return;             //Была нажата отмена
            //Иначе установить пользовательские цвета
            jl_one.setBackground(color_model[0]);
            jl_two.setBackground(color_model[0]);
            jl_bomb.setBackground(color_model[0]);
            jl_button.setBackground(color_model[1]);
            jp_field.setBackground(color_model[2]);
        });
        //Применение темы
        OKButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switch(changer){
                    case 1:{
                        LoadSave.putParam("&bmf", 0x1d829b);
                        LoadSave.putParam("&btn", 0x99d9ea);
                        LoadSave.putParam("&lbn", 0x9ccca5);
                        break;
                    }
                    case 2:{
                        LoadSave.putParam("&bmf", 0x000000);
                        LoadSave.putParam("&btn", 0xffffff);
                        LoadSave.putParam("&lbn", 0x5b1111);
                        break;
                    }
                    case 3:{
                        LoadSave.putParam("&bmf", decToHex(jp_field.getBackground().getRGB()));
                        LoadSave.putParam("&btn", decToHex(jl_button.getBackground().getRGB()));
                        LoadSave.putParam("&lbn", decToHex(jl_one.getBackground().getRGB()));
                        break;
                    }
                    //default, если changer не соответствует ни одной схеме(подмена значения в Save-файле)
                    default:{
                        LoadSave.putParam("&bmf", 0x1d829b);
                        LoadSave.putParam("&btn", 0x99d9ea);
                        LoadSave.putParam("&lbn", 0x9ccca5);
                    }
                }
                LoadSave.putParam("&clm", changer);
                //Перерисовка минного поля согласно новой цветовой схеме
                for(int i=0; i<bombsfield.getComponentCount(); i++){
                    if(bombsfield.getComponent(i) instanceof JButton){
                        bombsfield.getComponent(i).setBackground(new Color(LoadSave.getParam("&btn")));
                    }
                    if(bombsfield.getComponent(i) instanceof JLabel){
                        bombsfield.getComponent(i).setBackground(new Color(LoadSave.getParam("&lbn")));
                    }
                }
                bombsfield.setBackground(new Color(LoadSave.getParam("&bmf")));
                dispose();
            }
        });
        setVisible(true);
    }
    //Обозначить текущюю тему
    private void setSelected(int changer){
        switch (changer){
            case 1:
                light_pan.setBackground(Color.PINK);
                dark_pan.setBackground(Color.DARK_GRAY);
                custom_pan.setBackground(Color.DARK_GRAY);
                editButton.setEnabled(false);
                break;
            case 2:
                light_pan.setBackground(Color.DARK_GRAY);
                dark_pan.setBackground(Color.PINK);
                custom_pan.setBackground(Color.DARK_GRAY);
                editButton.setEnabled(false);
                break;
            case 3:
                light_pan.setBackground(Color.DARK_GRAY);
                dark_pan.setBackground(Color.DARK_GRAY);
                custom_pan.setBackground(Color.PINK);
                editButton.setEnabled(true);
                break;
        }
    }
    //Перевод значения цвета из decimal в Hex
    private int decToHex(int tmp){

        String str  = Integer.toHexString(tmp);
        str = str.substring(str.length()-6); //последние 6 символов из 8(отсекаем значение alpha - первые два символа)
        return Integer.parseInt(str, 16);
    }



}
