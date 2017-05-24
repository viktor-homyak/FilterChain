package com.tutorials;

import java.util.Random;

/**
 * Created by vhomyak on 24.05.2017.
 */
public enum Names {
 Fedya,Igor,Petro,Ivan,Stepan,
    Ignat,Prokop, Oleksiy,Viktor,Sashko,
    Taras,Nazar, Robert,John,Timur,
    Vlad,Vitalii,Kiril,Ostap,Andriy,
    Slavko,Serhiy,Rostik, Oleg, Kostya;

    public static String getRandomName() {
        Random random = new Random();
        return values()[random.nextInt(values().length)].name();
    }


}
