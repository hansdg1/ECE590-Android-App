package edu.ksu.ece590.androideffectsdemo.renders;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.media.AudioTrack;
import android.util.AttributeSet;
import android.view.View;


import java.util.Stack;

import edu.ksu.ece590.androideffectsdemo.sounds.SoundPCM;


/**
 * Created by el on 4/24/2015.
 */
public class CustomDrawableView extends View {

    public enum RenderState
    {

        PROGRESS_BAR,
        AMPLITUDE_PLOT

    }

    RenderState renderState = RenderState.AMPLITUDE_PLOT;

    int framesPerSecond = 60;
    long startTime;

    float totalTimeMs;

    AudioTrack audioTrack;
    SoundPCM soundData;

    Matrix matrix = new Matrix();
    Paint paint = new Paint();

    int previousState;
    float timePosition;


    int samplesSinceLastDraw = 0;

    int samplesPerDraw = 200;




    Stack<PointF> drawableStack = new Stack<PointF>();

    public CustomDrawableView(Context context) {
        super(context);
        Setup();
    }

    public CustomDrawableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Setup();
    }


    public void Setup() {

        paint.setColor(Color.GREEN);

        this.startTime = System.currentTimeMillis();
        this.postInvalidate();
     }


    protected void onDraw(Canvas canvas) {
        // Calculate points for line

        long elapsedTime = System.currentTimeMillis() - startTime;







        if(audioTrack != null){

            if(previousState == AudioTrack.PLAYSTATE_PLAYING && audioTrack.getPlayState() == AudioTrack.PLAYSTATE_STOPPED){
                int i = 0;
                i++;
            }
            if(audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING)
            {
                previousState = AudioTrack.PLAYSTATE_PLAYING;

                timePosition = (float)audioTrack.getPlaybackHeadPosition() / (float)audioTrack.getSampleRate() * 1000;




                if(renderState == RenderState.PROGRESS_BAR)
                {

                    float xPos = (timePosition / totalTimeMs) * (float)getWidth();


                  //  canvas.drawLine(0, getHeight() / 2, xPos  , getHeight() / 2, paint);
                    canvas.drawRect(0, getHeight() / 2 - 25, xPos, getHeight() / 2 + 25, paint);
                }

                if(renderState == RenderState.AMPLITUDE_PLOT){

                    float xPos = (timePosition / totalTimeMs) * (float)getWidth();


                    //max height
                    int maxHeight = getHeight();
                    int dc = maxHeight / 2;


                    int maxData = 32767;

                    int elapsedSamples = audioTrack.getPlaybackHeadPosition() - samplesSinceLastDraw;




                    int data = soundData.GetValueAtIndex(audioTrack.getPlaybackHeadPosition());

                    float normalized = (float)data / (float)maxData;


                    if(elapsedSamples >= samplesPerDraw)
                    {
                        samplesSinceLastDraw = audioTrack.getPlaybackHeadPosition();
                       drawableStack.push(new PointF(xPos, normalized));
                    }

                    PointF previous = new PointF(0,0);
                    int index = 0;
                    for(PointF obj : drawableStack)
                    {
                        if(index > 0)
                        {
                            canvas.drawLine(previous.x,getHeight() - dc - previous.y * getHeight(),obj.x,getHeight() - dc - obj.y*getHeight(), paint);

                        }
                        previous = obj;
                    index++;
                    }


                }



            }else
            {
                //canvas.drawLine(getWidth()/ 2, 0, getWidth() / 2, getHeight(), paint);
            }





        }else {

        }

        this.startTime = elapsedTime;

        this.postInvalidateDelayed( 1000 / framesPerSecond);

    }

    public void update(AudioTrack audioTrack, SoundPCM soundData)
    {
        this.audioTrack = audioTrack;
        this.soundData = soundData;

         totalTimeMs = (float)soundData.NumberOfSamples()/ (float)soundData.SampleRate() * 1000;

        previousState = AudioTrack.PLAYSTATE_STOPPED;

        drawableStack.clear();
        samplesSinceLastDraw = 0;

    }
}
