package edu.ksu.ece590.androideffectsdemo.sounds;

/**
 * Created by dab on 3/11/2015.
 */
public class SoundPCM {

    short[] buffer;

    public SoundPCM(){
        buffer = new short[10];
    }

    public SoundPCM(short[] input){
        buffer = input;
    }

    public int NumberOfSamples() {
        return buffer.length;
    }

    public short[] GetBuffer() {
        return buffer;
    }

    //Does not check buffer bounds
    public short GetValueAtIndex(int i) {
        return buffer[i];
    }

}
