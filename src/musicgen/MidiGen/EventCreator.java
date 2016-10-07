/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicgen.MidiGen;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;

/**
 *
 * @author FurEterinUmbrae
 */
public class EventCreator {
    
    public static MidiEvent createMidiEvent(MidiMessage mm, long time){
        return new MidiEvent(mm,time);
    }
    
}
