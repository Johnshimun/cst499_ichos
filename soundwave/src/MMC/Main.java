package MMC;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
//import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
//import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;

public class Main
{
	public static void main(String[] args)
	{
		System.out.println("Start sound test");
		try
		{
			AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED ,44100, 16, 2, 4, 44100, false);
			// (format of audio, sample rate(highest is 44100),sample size in bits,number of channels 1=mono 2=stereo,framesize in bytes,framerate,false for not big-endian )
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format); // get a target dataline from the system
			if(!AudioSystem.isLineSupported(info)) {System.err.println("Line not supported"); }  //make sure the line is supported
			
			final TargetDataLine targetLine = (TargetDataLine)AudioSystem.getLine(info); //capture the line from audio system
			targetLine.open(); //open the line so it allocates it's system resources
			
			System.out.println("Start recording");
			targetLine.start();
			
			Thread thread = new Thread() 
			{
				@Override public void run()
				{
					AudioInputStream audioStream = new AudioInputStream(targetLine); // make an audio stream for the target line
					File audioFile = new File("record.wav"); //make file
					try {AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);}  //data stream writes to the file
					catch (IOException ioe) {ioe.printStackTrace();}
					System.out.println("Stopped Recording");
				}
			};
			
			thread.start();
			Thread.sleep(5000); // 5000 is in ms so 5 second record time
			targetLine.stop();
			targetLine.close();
			
			System.out.println("ended sound test");
				
		
		}
		catch (LineUnavailableException lue) { lue.printStackTrace();}
		
		catch(InterruptedException ie) {ie.printStackTrace();
	}
}
}