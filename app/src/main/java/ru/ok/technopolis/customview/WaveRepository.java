package ru.ok.technopolis.customview;

import java.util.Random;

public class WaveRepository {

    public static final int MAX_VOLUME = 100;
    private static final int WAVE_LENGTH = 200;

    public static int[] getWaveData() {
        int[] data = new int[WAVE_LENGTH];
        Random r = new Random();
        for (int i = 0; i < data.length; i++) {
            int value = r.nextInt(MAX_VOLUME + 1);
            data[i] = value;
        }
        return data;
    }

}
