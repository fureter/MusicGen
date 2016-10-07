/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicgen.PatternGeneration;

import java.util.ArrayList;
import java.util.Random;
import musicgen.MusicGen;

/**
 *
 * @author FurEterinUmbrae
 */
public class Node {
    
    public ArrayList<Edge> edge = new ArrayList<>();
    public int key;
    public long duration;
    
    public double sumWeight = 0;
    
    public Node(int key)
    {
        this.key = key;
        duration = MusicGen.dt;
    }
    
    public void addEdge(Edge e){
        double w = e.weight+sumWeight;
        sumWeight+=e.weight;
        
        e.weight=w;
        
        edge.add(e);
        
    }
    
    public boolean hasEdge(Node no){
        for(int i = 0; i < edge.size(); i++){
            if(edge.get(i).nextNode == no){
                return true;
            }
        }
        
        return false;
    }
    
    public Node nextNode(Random ran){
        double choice = ran.nextDouble()*sumWeight;
        int current = 0;
        
        for(int i = 1; i < edge.size();i++){
            if(Math.abs(edge.get(current).weight-choice) >  Math.abs(edge.get(i).weight-choice)){
                current = i;
            }
        }
        return edge.get(current).nextNode;
    }
    
}
