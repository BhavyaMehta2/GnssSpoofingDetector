package com.app.navicspoofdetector;

import android.location.GnssStatus;

import java.util.ArrayDeque;
import java.util.Deque;

public class GnssSat {

    private static final int WINDOW_LENGTH = 10;

    private int id;
    private double cn0;
    private String CF;
    private int constellation;
    private int hash;
    private final Deque<Double> signal;
    private final double[] result = new double[WINDOW_LENGTH];
    private double summationSqrRoot;
    private double mean;

    public int getHash() {
        return hash;
    }

    public void setHash() {
        this.hash = constellation * 10000 + id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCn0() {
        return cn0;
    }

    public void setCn0(double cn0) {
        this.cn0 = cn0;
    }

    public String getCF() {
        return CF;
    }

    public void setCF(String CF) {
        this.CF = CF;
    }

    public String getConstellation() {
        return constellationParser(constellation);
    }

    public void setConstellation(int constellation) {
        this.constellation = constellation;
    }

    public void setSignal() {
        if (signal.size() >= WINDOW_LENGTH) {
            signal.removeFirst();
        }
        signal.addLast(cn0);
        setConstants(signal);
    }

    private void setConstants(Deque<Double> arrayDeque) {
        double sum = 0;
        int count = arrayDeque.size();
        for (double num : arrayDeque) {
            sum += num;
        }
        mean = sum / count;

        int i = 0;
        summationSqrRoot = 0;
        for (double num : arrayDeque) {
            result[i++] = num - mean;
            summationSqrRoot += Math.pow(num - mean, 2);
        }

        summationSqrRoot = Math.sqrt(summationSqrRoot);
    }

    public double[] getNumeratorConstant() {
        return result;
    }

    public double getDenominatorConstant() {
        return summationSqrRoot;
    }

    public Deque<Double> getSignal() {
        while (signal.size() < WINDOW_LENGTH) {
            signal.addFirst(0.00);
        }
        return signal;
    }

    public double getMean() {
        return mean;
    }

    GnssSat() {
        super();
        this.signal = new ArrayDeque<>();
    }

    private String constellationParser(int constellation) {
        if (constellation == GnssStatus.CONSTELLATION_IRNSS)
            return "IRNSS";
        else if (constellation == GnssStatus.CONSTELLATION_GPS)
            return "GPS";
        else if (constellation == GnssStatus.CONSTELLATION_BEIDOU)
            return "BEIDOU";
        else if (constellation == GnssStatus.CONSTELLATION_GALILEO)
            return "GALILEO";
        else if (constellation == GnssStatus.CONSTELLATION_GLONASS)
            return "GLONASS";
        else if (constellation == GnssStatus.CONSTELLATION_QZSS)
            return "QZSS";
        else
            return "UNKNOWN";
    }
}
