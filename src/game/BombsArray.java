package game;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import resources.LoadSave;

//Создание массива данных
public class BombsArray {
    private  static Map<Integer, Integer> data;
    public BombsArray(){
        int n = LoadSave.getParam("&nnn");
        int m = LoadSave.getParam("&mmm");
        int max_bombs = LoadSave.getParam("&bmb");
        int date[][] = new int[m][n];
        data = new HashMap<>();

        int start = 0;
        //Заполнить временный массив нулями
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                date[j][i] = 0;
            }
        }
        //Генерируем позиции мин
        while(start<max_bombs){
            int rand_n = new Random().nextInt(n);
            int rand_m = new Random().nextInt(m);


            if(date[rand_m][rand_n] !=0) continue;  //позиция уже занята, пропуск
            date[rand_m][rand_n] = -1;              // -1  = мина
            start++;
        }
        start = 0;
        //Вычисление и запись значения количества мин в смежных ячейках
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++){
                if(date[j][i] != -1) {
                    int sum = 0;
                    for(int k=-1; k<2; k++){
                        for(int l=-1; l<2; l++){
                            try{
                                if(date[j+k][i+l] == -1) sum++;
                            }catch(ArrayIndexOutOfBoundsException e){
                                /*Пропуск несуществующего индекса*/
                            }
                        }
                    }
                    data.put(start, sum);           //Запись значения ячейки с ключем  = zOrder
                }else data.put(start, -1);
                start++;
            }
        }
    }
    //Доступ к массиву данных
    public  static Map<Integer, Integer> getMap(){
        return data;
    }
}

