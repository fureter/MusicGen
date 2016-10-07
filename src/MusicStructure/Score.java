/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicStructure;

import java.util.ArrayList;
import java.util.HashMap;
import javax.sound.midi.Track;
import musicgen.MidiGen.EventCreator;
import musicgen.MidiGen.MessageCreator;
import musicgen.MusicGen;

/**
 *
 * @author FurEterinUmbrae
 */
public class Score {
    
    private ArrayList<Measure> measures = new ArrayList<>();
    private HashMap sections = new HashMap();
    public int counter = 0;
    
    public Score(){
        System.out.println("Score Created");
    }
    
    public void addMeasure(Measure m){
        measures.add(m);
    }
    
    public void addSection(ArrayList m){
        sections.put(counter, m);
        counter++;
    }
    
    public ArrayList getSection(int index){
        return (ArrayList)sections.get(index);
    }
    
    public void makeMidiFile(Track t){
        
        System.out.println("Beginning Writing to Track");

        for(int i = 0; i < measures.size();i++){
            for(int j = 0; j < measures.get(i).notes.size();j++){
                t.add(EventCreator.createMidiEvent(MessageCreator.createShortMessage(measures.get(i).notes.get(j).note, MusicGen.NOTE_ON, 32),measures.get(i).notes.get(j).start));
                t.add(EventCreator.createMidiEvent(MessageCreator.createShortMessage(measures.get(i).notes.get(j).note, MusicGen.NOTE_OFF, 32),measures.get(i).notes.get(j).start+measures.get(i).notes.get(j).duration));
            }
        }
        
        
        System.out.println("Finished Writing to Track");
    }
    
}
