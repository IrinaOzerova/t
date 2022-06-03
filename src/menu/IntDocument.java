package menu;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

//Класс форматирования текстового поля JTextField
public class IntDocument extends PlainDocument {
       private int max_length;                          //Максимальное количество символов
    IntDocument(int max_length){
        this.max_length = max_length;
    }
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null || !Character.isDigit(str.charAt(0)))   //Не обрабатывать пустое или нечисловое значение
            return;
        if ((getLength() + str.length()) <= max_length) {

            super.insertString(offset, str, attr);
        }
    }
}
