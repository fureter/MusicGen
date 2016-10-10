/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicgen.MidiGen;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Track;
import musicgen.MusicGen;

/**
 *
 * @author FurEterinUmbrae
 */
public class EventCreator {
    
    /*
    * creates and returns a MidiEvent.
    */
    
    public static MidiEvent createMidiEvent(MidiMessage mm, long time){
        return new MidiEvent(mm,time);
    }
    
    /*
    * Manages the initial set up of Tracks for midi, setting the tempo, naming, polypressure, and omni.
    */
    public static void initDefualt(Track t, int channelNum){
        try{
            byte[] b = {(byte)0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte)0xF7};
            SysexMessage sm = new SysexMessage();
            sm.setMessage(b, 6);
            MidiEvent me = new MidiEvent(sm,(long)0);
            t.add(me);
            
            
            
            MetaMessage mt = new MetaMessage();
            byte[] bt = {5, 127, 0};
            mt.setMessage(0x51+channelNum ,bt, 3);
            me = new MidiEvent(mt,(long)0);
            t.add(me);
            
            mt = new MetaMessage();
            String TrackName = new String("Track" + (channelNum+1));
            mt.setMessage(0x03 ,TrackName.getBytes(), TrackName.length());
            me = new MidiEvent(mt,(long)0);
            t.add(me);
            
            //****  set omni on  ****
            ShortMessage mm = new ShortMessage();
            mm.setMessage(0xB0+channelNum, 0x7D,0x00);
            me = new MidiEvent(mm,(long)0);
            t.add(me);
            //****  set poly on  ****
            mm = new ShortMessage();
            mm.setMessage(0xB0+channelNum, 0x7F,0x00);
            me = new MidiEvent(mm,(long)0);
            t.add(me);
            
            mt = new MetaMessage();
            byte[] bet = {}; // empty array
            mt.setMessage(0x2F+channelNum,bet,0);
            me = new MidiEvent(mt, MusicGen.length);
            t.add(me);
            
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    /*
    * Sets the desired Instrument for track, using the instrument number and channelNumber for the track.
    */
    public static void setInstrument(Track t, int instrument, int channelNum){
        try{
        ShortMessage mm = new ShortMessage();
        mm.setMessage(0xC0+channelNum, instrument, 0x00);
        MidiEvent me = EventCreator.createMidiEvent(mm,(long)0);
        t.add(me);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
