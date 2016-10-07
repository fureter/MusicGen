/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicStructure;

import java.util.ArrayList;
import musicgen.MusicGen;

/**
 *
 * @author FurEterinUmbrae
 */
public class Measure {
    public long timeStart;
    private long length;
    
    public ArrayList<Note> notes = new ArrayList<>();
    
    public Measure(long start, long length){
        this.timeStart = start;
        this.length = length;
    }
    
    public Measure(long start,int[] note){
        this.timeStart = start;
        this.length = MusicGen.dt*note.length + start;
        for(int i = 0; i < note.length;i++){
            addNote(note[i],MusicGen.dt,timeStart+MusicGen.dt*i);
        }
    }
    
    public Measure(long start,int[][] note){
        this.timeStart = start;
        this.length = MusicGen.dt*note.length + start;
        //long time = 0;
        for(int i = 0; i < note.length;i++){
            addNote(note[i][0],note[i][1]*MusicGen.dt,note[i][2]*MusicGen.dt);
            //time += note[i][1]*MusicGen.dt;
        }
    }
    
    public void addNote(int note, long duration, long start){
        notes.add(new Note(note,duration,start));
    }
    
}
