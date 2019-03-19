package ru.ok.technopolis.customview;


// https://github.com/ajbanks/arff-builder/blob/3fc8f6860a37d33cd93081a9c8c0025fd70872f5/src/stretching/LinearInterpolation.java
public class LinearInterpolation {

    public static int[] interpolateArray(int[] source, int destinationLength) {
        int[] destination = new int[destinationLength];
        destination[0] = source[0];
        int jPrevious = 0;
        for (int i = 1; i < source.length; i++) {
            int j = i * (destination.length - 1) / (source.length - 1);
            interpolate(destination, jPrevious, j, source[i - 1], source[i]);
            jPrevious = j;
        }
        return destination;
    }

    private static void interpolate(int[] destination, int destFrom, int destTo, double valueFrom, double valueTo) {
        int destLength = destTo - destFrom;
        double valueLength = valueTo - valueFrom;
        for (int i = 0; i <= destLength; i++) {
            destination[destFrom + i] = (int) (valueFrom + (valueLength * i) / destLength);
        }
    }

}
