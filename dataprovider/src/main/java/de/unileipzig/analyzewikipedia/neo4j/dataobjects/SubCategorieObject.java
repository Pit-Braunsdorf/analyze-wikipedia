package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.unileipzig.analyzewikipedia.neo4j.constants.AnnotationKeys;
import org.neo4j.driver.v1.types.Node;

public class SubCategorieObject implements INodeObject {

    private final NodeType _type = NodeType.SubCategorie;
    private Map<String, Object> _annotations;
    private String _uid;

    private SubCategorieObject() {
        _annotations = new HashMap<>();
    }

    @Override
    public NodeType GetType() {
        return _type;
    }

    @Override
    public Map<String, Object> GetAnnotations() {
        return _annotations;
    }

    @Override
    public void AddAnnotation(String key, Object value) {
        _annotations.put(key, value);
    }

    public static SubCategorieObject CreateSubCategorieObject() {
        SubCategorieObject sub = new SubCategorieObject();

        sub._uid = UUID.randomUUID().toString();
        sub.AddAnnotation(AnnotationKeys.UID, sub._uid);

        return sub;
    }

    @Override
    public String GetUUID() {
        return _uid;
    }

    static INodeObject FromNode(Node node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
