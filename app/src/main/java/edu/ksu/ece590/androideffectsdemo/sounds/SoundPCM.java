package edu.ksu.ece590.androideffectsdemo.sounds;

/**
 * Created by dab on 3/11/2015.
 *
 * 3/16 added sample rate so we can track what the recorded rate was
 */
public class SoundPCM {

    short[] buffer;
    int SampleRate;

    //default constructor
    public SoundPCM(){
        buffer = new short[10];
        SampleRate = 44100;
    }
    //copy constructor
    public SoundPCM(SoundPCM aSoundPCM)
    {
        this(aSoundPCM.GetBuffer(), aSoundPCM.SampleRate());
    }

    //alternate constructor
    public SoundPCM(short[] input, int sampleRate){
        buffer = input;
        SampleRate = sampleRate;
    }

    public int NumberOfSamples() {
        return buffer.length;
    }

    public int SampleRate() { return SampleRate; }

    public short[] GetBuffer() {
        return buffer;
    }

    //Does not check buffer bounds
    public short GetValueAtIndex(int i) {
        return buffer[i];
    }

}
