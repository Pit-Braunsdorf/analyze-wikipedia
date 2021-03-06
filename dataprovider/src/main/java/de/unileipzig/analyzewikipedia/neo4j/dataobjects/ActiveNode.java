/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.List;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

/**
 *
 * @author Pit.Braunsdorf
 */
@NodeEntity(label="Active")
public final class ActiveNode extends Entity{
    
    @Property(name = "title")
    private String title = "Active";
    
    @Override
    public String getTitle() {
        return title;
    }
    
    @Relationship(type = "isActive", direction = Relationship.INCOMING)
    List<Entity> activeEntities;

    public ActiveNode() {
        this.title = "Active";
    }
    
}

