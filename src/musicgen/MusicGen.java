/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicgen;

//import org.lwjgl.LWJGLException;

import MusicStructure.Scale;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Track;
import musicgen.MidiGen.EventCreator;
import musicgen.MidiGen.MessageCreator;


/**
 *
 * @author FurEterinUmbrae
 */
public class MusicGen {

    public static Sequencer seq;
    
    //time data for piece. total length isn't too important currenty, it needs to be refactored.
    public static final long dt = 120;
    public static long time = 0;
    public static long length = 19200;
    
    //saved midi status messages for note on and note off. used often enough that its nice to have here
    public static  final int NOTE_ON = 0x90;
    public static  final int NOTE_OFF = 0x80;
    
    //creates the random number generator for the program, uses current time as seed, but could be changed for more control.
    public static long seed = System.currentTimeMillis();
    public static Random ran = new Random(seed);
    
    //probablities for octave volitility, and note length volitility.
    //higher lenvolt will produce more quarter notes, higher volitility will produce more octave shifts
    public static double volitility = .06;
    public static double lenVolt = .4;
    
    //maximum number of repeating sequences. not exactly working.
    public static int MaxRepeat = 4;
    
    //used for keeping track of midi channels.
    public  static int track1channel = 0;
    public static int track2channel = 1;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //initializes static data for MessageCreator, and Scale class.
        MessageCreator.init();
        Scale.init();
        
        try{
            seq = MidiSystem.getSequencer();
            seq.open();
            Sequence sq = new Sequence(Sequence.PPQ,120);
            
        //Sets up the two tracks that will be played, initializing them, and setting their instuments    
        
            Track t = sq.createTrack();
            EventCreator.initDefualt(t, 0);
            EventCreator.setInstrument(t, 8, 0);
            
            Track t2 = sq.createTrack();
            EventCreator.initDefualt(t2, 1);
            EventCreator.setInstrument(t2, 11, 1);
            
            
            System.out.println("Begining Markov");

            ProceduralMusic.markovSectionated(t,t2, "d#5","d#4", "Major", 300, 8, 4, 50, ran);
            
            System.out.println("Finished Markov");

            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            //opening a file called MarkovMidi4 to write the midi data to
            File f = new File("MarkovMidi4.mid");
            //Writes the data to file f
            MidiSystem.write(sq,1,f);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        System.out.println("Done"); 
        
        //immediatly opens and playins the midi file using MidiYodi in my directory, comment this out when you run it.
        try {
            System.out.println("opening midi file");
            Runtime.getRuntime().exec("C:\\Users\\FurEterinUmbrae\\Downloads\\MidiYodi-3.1.exe MarkovMidi4.mid");
        } catch (IOException ex) {
            Logger.getLogger(MusicGen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.exit(0);
    }
    
}
