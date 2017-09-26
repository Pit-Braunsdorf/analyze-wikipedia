/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.service;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.Entity;
import java.util.List;

/**
 *
 * @author Pit.Braunsdorf
 * @param <T>
 */
public interface Service<T extends Entity> {
    Iterable<T> findAll();
    
    T find(long id);
    //T find(String title);
    
    void delete(long id);
    //void delete(String title);
    
    T createOrUpdate(T object);
    
    T findByTitle(String title);
    
    Iterable<Entity> shortestPathTo(String sourceTitle, String destinationType, String destinationTitle);
    
    /**
     * Gibt alle Knoten zur�ck, auf die dieser verlinkt.
     * 
     * @param title Titel des Knotens
     * @return 
     */
    Iterable<Entity> getAllLinkedNodes(String title);
    
    /**
     * Gibt Anzahl der Knoten zur�ck
     * 
     * @return 
     */
    Integer getNodeCounter();
    
    /**
     * Gibt Anzahl alle Knoten des bestimmten Types zur�ck
     * 
     * @param label Label der Knoten
     * @return 
     */
    Iterable<Entity> getNodesWithLabel(String label);
    
    /**
     * Gibt alle Knoten ohne Verbindung zu anderen Knoten zur�ck
     * 
     * @return 
     */
    Iterable<Entity> getAllNodesWithoutConnection();
    
    /**
     * Gibt alle Knoten mit angegebenner Verbindung zur�ck
     * 
     * @param type Typ der Relation
     * @return 
     */
    Iterable<Entity> getAllNodesWithConnection(String type);
    
    /**
     * Gibt alle Knoten mit angegebenner Typ und suchvariable f�r den Title zur�ck
     * 
     * @param sequence Label der Knoten
     * @param type Typ der Relation
     * @return 
     */
    Iterable<Entity> getAllNodesByLabelAndSequence(String label, String sequence);
}
