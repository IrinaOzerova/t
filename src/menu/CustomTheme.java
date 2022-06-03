package menu;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

//Кастомайзер минного поля

public class CustomTheme extends JDialog {
    private JPanel custom_img;
    private JLabel l2;
    private JLabel bmb;
    private JLabel btn;
    private JLabel l1;
    private JPanel pan;
    private JSlider red_color;
    private JSlider green_color;
    private JSlider blue_color;
    private JTextField red_field, green_field, blue_field;
    private JButton pickerButton;
    private JLabel jl_setting;
    private JButton okay;
    private JButton cancelButton;
    private Color clr_chooser;
    private Color[] color_array;

    CustomTheme(JDialog jd, Color l_color, Color button_color, Color field_color) {
        super(jd, "Color chooser", true);
        l1.setBackground(l_color);
        l2.setBackground(l_color);
        bmb.setBackground(l_color);
        btn.setBackground(button_color);
        custom_img.setBackground(field_color);
        Border bf = BorderFactory.createLineBorder(Color.red);
        l1.setBorder(bf);
        l2.setBorder(bf);
        bmb.setBorder(bf);
        btn.setBorder(bf);
        setContentPane(pan);
        setUndecorated(false);
        setSize(800, 300);
        setResizable(false);
        setLocationRelativeTo(jd);
        pack();

        jl_setting.setText(custom_img.getToolTipText());                //текст метки берется из всплывающей подсказки
        clr_chooser = custom_img.getBackground();
        setColor(clr_chooser);                                          //стартовые значения слайдеров
        //Форматирование вывода текстовых полей
        red_field.setDocument(new IntDocument(3));
        green_field.setDocument(new IntDocument(3));
        blue_field.setDocument(new IntDocument(3));
        //Стартовые значения текстовых полей
        red_field.setText(Integer.toString(clr_chooser.getRed()));
        green_field.setText(Integer.toString(clr_chooser.getGreen()));
        blue_field.setText(Integer.toString(clr_chooser.getBlue()));

        //Слушатели событий
        red_color.addChangeListener((ce)-> {
            setField(red_field, red_color);
        });
        green_color.addChangeListener((ce)-> {
                setField(green_field, green_color);
        });
        blue_color.addChangeListener((ce)-> {
            setField(blue_field, blue_color);
        });
        btn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                colorSetter(btn);

            }
        });
        l1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                colorSetter(l1);

            }
        });
        l2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                colorSetter(l1);

            }
        });
        bmb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                colorSetter(l1);

            }
        });
        custom_img.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                jl_setting.setText(custom_img.getToolTipText());
                clr_chooser = custom_img.getBackground();
                setColor(clr_chooser);
            }
        });
        pickerButton.addActionListener((ae) -> {
                //JColorChooser.class библиотеки swing
            Color tmp_clr = JColorChooser.showDialog(CustomTheme.this, "Select color", clr_chooser);
            if(tmp_clr == null) tmp_clr = clr_chooser;  //была нажата кнопка отмены
            setColor(tmp_clr);
        });

        //При клике в текстовом поле выделить текст
        red_field.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                red_field.selectAll();
            }
        });
        green_field.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                red_field.selectAll();
            }
        });
        blue_field.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                red_field.selectAll();
            }
        });

        red_field.addActionListener( (ae) ->{
            setSlider(red_field, red_color);
        });
        green_field.addActionListener( (ae) ->{
            setSlider(green_field, green_color);
        });
        blue_field.addActionListener( (ae) ->{
            setSlider(blue_field, blue_color);
        });




        //Возврат в окно Themes и установка цветов
        okay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                color_array = new Color[3];
                color_array[0] = l1.getBackground();
                color_array[1] = btn.getBackground();
                color_array[2] = custom_img.getBackground();
                dispose();
            }
        });
        //Отмена действий
        cancelButton.addActionListener((ActionEvent ae) ->{

            dispose();
        });


        setVisible(true);


    }

    //Установка текстового поля и отображение цвета на макете минного поля
    private void setField(JTextField field, JSlider js){

        field.setText(Integer.toString(js.getValue()));
        int r = red_color.getValue();
        int g = green_color.getValue();
        int b = blue_color.getValue();


        clr_chooser = new Color(r, g, b);
        switch (jl_setting.getText()){
            case "Background":
                custom_img.setBackground(clr_chooser);
                break;
            case "NumberBombField":
                l1.setBackground(clr_chooser);
                l2.setBackground(clr_chooser);
                bmb.setBackground(clr_chooser);
                break;
            case "Button":
                btn.setBackground(clr_chooser);
                break;
        }
    }
    //Клик по макету минного поля
    private void colorSetter(JLabel jl){
        jl_setting.setText(jl.getToolTipText());
        clr_chooser = jl.getBackground();
        //Установить значения слайдеров
        setColor(clr_chooser);
    }
    //Установка слайдера из поля JTextField
    private void setSlider(JTextField tf, JSlider js){
        if(Integer.parseInt(tf.getText()) > 255) {
            js.setValue(255);
            tf.setText("255");
            return;
        }
        js.setValue(Integer.parseInt(tf.getText()));
    }
    //Установка слайдера при первом запуске и кнопке picker
    private void setColor(Color clr){
        int r = clr.getRed();
        int g = clr.getGreen();
        int b = clr.getBlue();
        red_color.setValue(r);
        green_color.setValue(g);
        blue_color.setValue(b);
    }
    //Возврат массива или null в окно Theme
    public static Color[] showDialog(JDialog jd, Color l1_clr, Color bmb_clr, Color fld_clr){
        CustomTheme ctheme = new CustomTheme(jd, l1_clr, bmb_clr, fld_clr);

        return ctheme.color_array;
    }
}

