package goksoft.chat.app.audioRecording;

import goksoft.chat.app.ControllerRules;
import goksoft.chat.app.errorClass.ErrorResult;
import goksoft.chat.app.errorClass.Result;
import goksoft.chat.app.errorClass.SuccessResult;

import javax.sound.sampled.*;
import java.io.*;
import java.lang.annotation.Target;

public class JavaSoundRecorder {
    static final long RECORD_TIME = 60000; //1 minute

    //path of wav file
    String filePath = FileHelper.createPath("wav");
    File wavFile = new File(filePath);

    //format of audio file
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

    //the line from which audio data is captured
    TargetDataLine line;
    /**
     * Defines an audio format
     */
    AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
        return format;
    }

    Result startRecording(){
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            Result result = ControllerRules.Run(checkIfSystemNotSupport(info));
            if(result != null){
                return result;
            }

            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start(); //start capturing

            System.out.println("Start capturing...");

            AudioInputStream audioInputStream = new AudioInputStream(line);

            System.out.println("Start recording...");

            //start recording
            AudioSystem.write(audioInputStream, fileType, wavFile);
        }catch (LineUnavailableException ex){
            ex.printStackTrace();
            return new ErrorResult();
        }catch (IOException ioe){
            ioe.printStackTrace();
            return new ErrorResult();
        }

        return new SuccessResult();
    }

    Result finish(){
        line.stop();
        line.close();
        return new SuccessResult("Finished");
    }

    private Result checkIfSystemNotSupport(DataLine.Info info){
        //checks if system supports the data line
        if(!AudioSystem.isLineSupported(info)){
            return new ErrorResult("Line not supported");
        }
        return new SuccessResult();
    }

}

/*Example usage

    Entry to run the program

    public static void main(String[] args) {
        final JavaSoundRecorder recorder = new JavaSoundRecorder();

        // creates a new thread that waits for a specified
        // of time before stopping
        Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(RECORD_TIME);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                recorder.finish();
            }
        });

        stopper.start();

        // start recording
        recorder.start();
    }
*/