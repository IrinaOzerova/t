package resources;



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

//Загрузка и сохранение данных
public class LoadSave {
    private static Map<String, Integer> param;
    private  Map<String, Integer> def_param;
    private static ArrayList<String> arr_easy, arr_medium, arr_hard;


    public LoadSave() {
        param = new HashMap<>();
        def_param = new HashMap<>();
        //База по умолчанию
        def_param.put("&nnn", 10);
        def_param.put("&mmm", 10);
        def_param.put("&bmb", 10);
        def_param.put("&bmf", 0x1d829b);
        def_param.put("&btn", 0x99d9ea);
        def_param.put("&lbn", 0x9c9ca5);
        def_param.put("&gmd", 1);
        def_param.put("&clm", 1);
        //Топ 10
        arr_easy = new ArrayList<>(10);
        arr_medium = new ArrayList<>(10);
        arr_hard = new ArrayList<>(10);


        readParamFile();
        readResultsFile();
    }
    //Параметры запуска приложения
    private void readParamFile(){
        int temp;
        try(FileInputStream fr = new FileInputStream("D:/JavaProjects/JavaProjects/JavaProjects/src/resources/savegame.sav")){
            byte[] content = new byte[fr.available()];
            fr.read(content);
            String[] str = new String(content).split("\n");

            for(String line : str){
                String sub = line.substring(0, 4);
                //Отсутствие ключа в базе по умолчанию приведет к исключению NullpointerException
                temp = def_param.get(sub);
                writeMap(sub, line.substring(4), temp);
            }
        }catch(IOException | NullPointerException io){             //Установить значения по умолчанию
            param.putAll(def_param);
        }
    }
    //Данные топ 10
    private void readResultsFile() {
        try (FileInputStream fr = new FileInputStream("D:/JavaProjects/JavaProjects/JavaProjects/src/resources/results.sav")) {
            byte[] content = new byte[fr.available()];
            fr.read(content);
            String[] str = new String(content).split("\n");

            for (String line : str) {
                String sub = line.substring(0,1 );
                switch(sub){

                    case "e":
                        arr_easy.add(line.substring(1).trim());
                        break;
                    case "m":
                        arr_medium.add(line.substring(1).trim());
                        break;
                    case "h":
                        arr_hard.add(line.substring(1).trim());
                        break;
                    default: break;

                }

            }
        }catch (IOException | StringIndexOutOfBoundsException io) {
            //Do nothing... Ignore
        }
        //Упорядочить по возрастанию
        Collections.sort(arr_easy);
        Collections.sort(arr_medium);
        Collections.sort(arr_hard);

    }
    //Получение параметра
    public static int getParam(String key){
        return param.get(key);

    }
    //Запись параметра
    public static void putParam(String key, Integer value) {
        param.put(key, value);
    }

    //Запись в карту
    private void writeMap(String key, String value, int defalut){
        try{
            int tmp = Integer.parseInt(value, 16);
            param.put(key, tmp);
        }catch(NumberFormatException nfe){      //Не удалось получить число... Значение по умолчанию
            param.put(key, defalut);
        }

    }
    //Получить значение из массива
    public static String getArray(int i, int pos){
        String value = "";
        try {
            switch (i) {
                case 1:
                    value = arr_easy.get(pos);
                    break;
                case 2:
                    value = arr_medium.get(pos);
                    break;
                case 3:
                    value = arr_hard.get(pos);
                    break;
            }

        }catch (IndexOutOfBoundsException ie){
            value = "";
        }
        return value;
    }
    //Записать значение в массив
    public static void putArray(int i, int pos, String value){
        switch (i) {
            case 1:
                arr_easy.add(pos, value);
                arr_easy.trimToSize();      //Обрезать до размера 10
                break;
            case 2:
                arr_medium.add(pos, value);
                arr_medium.trimToSize();
                break;
            case 3:
                arr_hard.add(pos, value);
                arr_hard.trimToSize();
                break;
        }
    }

    //Сохранение параметров
    public static void saveParam(){
        try (FileOutputStream fs = new FileOutputStream("D:/JavaProjects/JavaProjects/JavaProjects/src/resources/savegame.sav")) {

            String tmp = "";
            byte[] buffer;
            for (Map.Entry<String, Integer> entry : param.entrySet()) {
                tmp += entry.getKey() + Integer.toHexString(entry.getValue())+"\n";

            }
            buffer = tmp.getBytes();
            fs.write(buffer);
        } catch (IOException e) {
            System.out.println("save error");
        }
    }
    //Сохранение результатов игры(top10)
    public static void saveResults(){
        try (FileOutputStream fs = new FileOutputStream("D:/JavaProjects/JavaProjects/JavaProjects/src/resources/results.sav")) {

            String tmp = "";
            byte[] buffer;
            for (String itr : arr_easy)
                tmp = tmp +"e"+itr+"\n";
            for (String itr : arr_medium)
                tmp = tmp + "e"+itr+"\n";

            for (String itr : arr_hard)
                tmp = tmp +"e"+itr+"\n";

            buffer = tmp.getBytes();
            fs.write(buffer);
        } catch (IOException e) {
            System.out.println("save error");
        }
    }
}
