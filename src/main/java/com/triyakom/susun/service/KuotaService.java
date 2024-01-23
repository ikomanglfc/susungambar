package com.triyakom.susun.service;

import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
public class KuotaService {
    Map<Integer, Integer> kuotas = new HashMap<>();
    Map<Integer, Integer> happyKuotas = new HashMap<>();

    public KuotaService(){
        kuotas.put(0, 0);
        kuotas.put(1, 300);
        kuotas.put(2, 400);
        kuotas.put(3, 500);
        kuotas.put(4, 600);
        kuotas.put(5, 750);
        kuotas.put(6, 1024);
        kuotas.put(7, 10240);

        happyKuotas.put(0, 0);
        happyKuotas.put(1, 300);
        happyKuotas.put(2, 400);
        happyKuotas.put(3, 500);
        happyKuotas.put(4, 600);
        happyKuotas.put(5, 950);
        happyKuotas.put(6, 1224);
        happyKuotas.put(7, 12488);

    }

    public boolean isHappyDay(){
        Calendar cal = Calendar.getInstance();

        boolean happy = true;

        //if(cal.get(Calendar.YEAR) > 2021) happy = false;
        //if(cal.get(Calendar.MONTH) > 10) happy = false;

        happy &= cal.get(Calendar.DAY_OF_WEEK) == 5;
//        return happy; TODO
        return false;
    }

    public int getMaxNumber(){
        return 7;
    }

    public int getKuota(int number){
        if(number < 0) number = 0;
        if(number > getMaxNumber()) number = getMaxNumber();
        return isHappyDay() ? happyKuotas.get(number) : kuotas.get(number);
    }

    public String getKuotaString(int number){
        return kuotaToString(kuotas.get(number));
    }

    public static String kuotaToString(int kuota){
        if(kuota < 1000){
            return kuota + " MB";
        }else if(kuota % 1024 == 0){
            return (kuota/ 1024) + " GB";
        }else{
            return (kuota/1024) + " GB + " + (kuota % 1024) + " MB";
        }
    }
}
