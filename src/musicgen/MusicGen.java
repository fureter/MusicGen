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
import musicgen.MidiGen.MessageCreator;


/**
 *
 * @author FurEterinUmbrae
 */
public class MusicGen {

    public static Sequencer seq;
    
    public static final long dt = 120;
    public static long time = 0;
    public static long length = 19200;
    
    public static  final int NOTE_ON = 0x90;
    public static  final int NOTE_OFF = 0x80;
    
    public static long seed = System.currentTimeMillis();
    public static Random ran = new Random(seed);
    
    public static double volitility = .06;
    public static double lenVolt = .5;
    
    public static int MaxRepeat = 4;
    
    public static int velLow = 30;
    public static int velHigh = 40;
    public static int velStep = 5;
    
    public  static int track1channel = 0;
    public static int track2channel = 1;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MessageCreator.init();
        Scale.init();
        
        try{
            seq = MidiSystem.getSequencer();
            seq.open();
            Sequence sq = new Sequence(Sequence.PPQ,120);
            
            
        
            Track t = sq.createTrack();
            byte[] b = {(byte)0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte)0xF7};
            SysexMessage sm = new SysexMessage();
            sm.setMessage(b, 6);
            MidiEvent me = new MidiEvent(sm,(long)0);
            t.add(me);
            
            
            
            MetaMessage mt = new MetaMessage();
            byte[] bt = {5, 127, 0};
            mt.setMessage(0x51 ,bt, 3);
            me = new MidiEvent(mt,(long)0);
            t.add(me);
            
            mt = new MetaMessage();
            String TrackName = new String("Piano Melody");
            mt.setMessage(0x03 ,TrackName.getBytes(), TrackName.length());
            me = new MidiEvent(mt,(long)0);
            t.add(me);
            
            //****  set omni on  ****
            ShortMessage mm = new ShortMessage();
            mm.setMessage(0xB0, 0x7D,0x00);
            me = new MidiEvent(mm,(long)0);
            t.add(me);
            //****  set instrument to Piano  ****
            mm = new ShortMessage();
            mm.setMessage(0xC0, 40, 0x00);
            me = new MidiEvent(mm,(long)0);
            t.add(me);
            //****  set poly on  ****
            mm = new ShortMessage();
            mm.setMessage(0xB0, 0x7F,0x00);
            me = new MidiEvent(mm,(long)0);
            t.add(me);
            
            
            
            
            Track t2 = sq.createTrack();
            byte[] b2 = {(byte)0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte)0xF7};
            SysexMessage sm2 = new SysexMessage();
            sm2.setMessage(b2, 6);
            MidiEvent me2 = new MidiEvent(sm2,(long)0);
            t2.add(me2);
            
            MetaMessage mt2 = new MetaMessage();
            byte[] bt2 = {6, 127, 0};
            mt2.setMessage(0x51+track2channel ,bt2, 3);
            me2 = new MidiEvent(mt2,(long)0);
            t2.add(me2);
            
            mt2 = new MetaMessage();
            String TrackName2 = new String("Piano Harmony");
            mt2.setMessage(0x03 ,TrackName2.getBytes(), TrackName2.length());
            me2 = new MidiEvent(mt2,(long)0);
            t2.add(me2);
            
            ShortMessage mm2 = new ShortMessage();
            mm2.setMessage(0xB0+track2channel, 0x7D,0x00);
            me2 = new MidiEvent(mm2,(long)0);
            t2.add(me2);
            
            mm2 = new ShortMessage();
            mm2.setMessage(0xB0+track2channel, 0x7F,0x00);
            me2 = new MidiEvent(mm2,(long)0);
            t2.add(me2);
            
            mm2 = new ShortMessage();
            mm2.setMessage(0xC0+track2channel, 41, 0x00);
            me2 = new MidiEvent(mm2,(long)0);
            t2.add(me2);
            
            /*
            for(long i = time; i < time+4*dt; i+=dt){
                t.add(EventCreator.createMidiEvent(MessageCreator.createShortMessage("c5", NOTE_ON, 0x40), i));
                t.add(EventCreator.createMidiEvent(MessageCreator.createShortMessage("c5", NOTE_OFF, 0x40), i+dt));
            }
            */
            
            //ProceduralMusic.createSimpleMusic(t, "c");
            
            System.out.println("Begining Markov");
            
            
            ProceduralMusic.markovSectionated(t,t2, "d#5","d#4", "Major", 300, 8, 4, 50, ran);
            
            System.out.println("Finished Markov");
            
                    
            mt = new MetaMessage();
            byte[] bet = {}; // empty array
            mt.setMessage(0x2F,bet,0);
            me = new MidiEvent(mt, length);
            t.add(me);
            
            mt2 = new MetaMessage();
            byte[] bet2 = {}; // empty array
            mt2.setMessage(0x2F,bet,0);
            me2 = new MidiEvent(mt2, length);
            t2.add(me2);
            
            
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            
            File f = new File("MarkovMidi4.mid");
		
            MidiSystem.write(sq,1,f);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        System.out.println("Done"); 
        
        try {
            System.out.println("opening midi file");
            Runtime.getRuntime().exec("C:\\Users\\FurEterinUmbrae\\Downloads\\MidiYodi-3.1.exe MarkovMidi4.mid");
        } catch (IOException ex) {
            Logger.getLogger(MusicGen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.exit(0);
    }
    
}
