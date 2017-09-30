/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.service;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.Entity;

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
    Iterable<Entity> getNodesWithoutConnection();
    
    /**
     * Gibt alle Knoten ohne Verbindung zu anderen Knoten zur�ck, ausser dem Activen Knoten
     * 
     * @return 
     */
    Iterable<Entity> getNodesWithOnlyActiveConnection();
    
    /**
     * Gibt alle aktiven Knoten zur�ck
     * 
     * @return 
     */
    Iterable<Entity> getActiveNodes();
    
    /**
     * Gibt alle Knoten mit angegebenner Verbindung zur�ck
     * 
     * @param type Typ der Relation
     * @param direction to or from node
     * @return 
     */
    Iterable<Entity> getNodesWithConnection(String type, String direction);
    
    /**
     * Gibt alle Knoten mit angegebenner Typ und suchvariable f�r den Title zur�ck
     * 
     * @param label Label der Knoten
     * @param sequence Suchstring f�r den Title
     * @return 
     */
    Iterable<Entity> getNodesByLabelAndTitlesequence(String label, String sequence);
    
    /**
     * Gibt alle Knoten mit angegebenner Verbindung zur�ck
     * 
     * @param label Label der Knoten
     * @param type Typ der Relation
     * @param direction Zu oder vom Knoten
     * @return 
     */
    Iterable<Entity> getLabeledNodesWithConnection(String label, String type, String direction);
    
    /**
     * Gibt alle Knoten zur�ck die von einem Subknoten verlinkt sind
     * 
     * @return 
     */
    Iterable<Entity> getSubNodesWithConnection();
    
    /**
     * Gibt k�rzesten Weg zwischen zwei Knoten zur�ck
     * 
     * @param start Titel des Startknotens
     * @param end Titel des Endknotens
     * @return 
     */
    Iterable<Entity> getShortestPath(String start, String end);
    
    /**
     * Gibt k�rzesten Weg zwischen zwei Knoten zur�ck, nur mit bestimmter Relation
     * 
     * @param start Titel des Startknotens
     * @param end Titel des Endknotens
     * @param type Relationstyp
     * @return 
     */
    Iterable<Entity> getShortestPath(String start, String end, String type);
    
    /**
     * Gibt Knoten mit gesuchter Beschriftung zur�ck
     * 
     * @param title Titel der Kante
     * @return 
     */
    Iterable<Entity> getNodesWithTitledConnection(String title);
}
