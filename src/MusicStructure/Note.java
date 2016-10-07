/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicStructure;

/**
 *
 * @author FurEterinUmbrae
 */
public class Note {
    public int note;
    public long duration;
    public long start;
    
    public Note(int note, long duration, long start){
        this.note =note;
        this.duration = duration;
        this.start = start;
    }
}
