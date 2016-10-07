/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicgen.PatternGeneration;

/**
 *
 * @author FurEterinUmbrae
 */
public class Edge {
    public double weight;
    public Node nextNode;
    
    public Edge(double weight,Node ne){
        this.nextNode = ne;
        this.weight = weight;
    }
}
