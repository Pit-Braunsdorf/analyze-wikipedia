/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.ui.webui.servlets;

import com.google.gson.Gson;
import de.unileipzig.analyzewikipedia.crawler.controller.WebCrawler;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.Entity;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ExternObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.service.ArticleService;
import de.unileipzig.analyzewikipedia.neo4j.service.ArticleServiceImpl;
import de.unileipzig.analyzewikipedia.neo4j.service.ExternService;
import de.unileipzig.analyzewikipedia.neo4j.service.ExternServiceImpl;
import de.unileipzig.analyzewikipedia.neo4j.service.SubArticleService;
import de.unileipzig.analyzewikipedia.neo4j.service.SubArticleServiceImpl;
import de.unileipzig.analyzewikipedia.textanalyse.MediaWikiLanguageHelper;
import de.unileipzig.analyzewikipedia.ui.webui.contracts.ArborViewModel;
import de.unileipzig.analyzewikipedia.ui.webui.contracts.EntityViewModel;
import de.unileipzig.analyzewikipedia.ui.webui.helper.MappingHelper;
import de.unileipzig.analyzewikipedia.ui.webui.helper.StringHelper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Pit.Braunsdorf
 */
@Path("/db")
public class DatabaseResource {

    @Context
    private UriInfo context;
    private ArticleService artService;
    private ExternService extService;
    private SubArticleService subArtService;

    private NodeType getTypeForEntity(Entity entity) {
        if (entity instanceof ArticleObject) {
            return NodeType.ARTICLE;
        }

        if (entity instanceof ExternObject) {
            return NodeType.EXTERN;
        }

        if (entity instanceof SubArticleObject) {
            return NodeType.SUBARTICLE;
        }

        return null;
    }

    private enum NodeType {
        ARTICLE, SUBARTICLE, EXTERN
    };

    /**
     * Creates a new instance of DatabaseResource
     */
    public DatabaseResource() {
        artService = new ArticleServiceImpl();
        extService = new ExternServiceImpl();
        subArtService = new SubArticleServiceImpl();
    }

    /**
     * Retrieves representation of an instance of
     * de.unileipzig.analyzewikipedia.ui.webui.servlets.DatabaseResource
     *
     * @param node
     * @param subArticle
     * @return an instance of java.lang.String
     */
    @GET
    //@QueryParam("subArticle")
    @Path("/all/{node}")
    @Produces("text/plain")
    public Response getJson(@PathParam("node") String node) {

        boolean subArticle = false;
        ArticleObject cur = artService.findByTitle(node);
        List<EntityViewModel> entities = new ArrayList<>();
        Hashtable<Long, HashSet<Long>> linkEdges = new Hashtable<>();
        Hashtable<Long, HashSet<Long>> hasEdges = new Hashtable<>();

        Iterable<Entity> subs = artService.getArticleAndAllSubArticles(cur.getTitle());
        Iterable<String> ext = artService.getWeblinks(cur.getTitle());

        List<EntityViewModel> all = MappingHelper.MapEntities(subs, cur.getTitle(), true);
        if (all.size() > 0) {

            HashSet<Long> hasTo = new HashSet<>();
            for (Entity sub : subs) {
                if (Objects.equals(sub.getId(), cur.getId())) {
                    continue;
                }

                hasTo.add(sub.getId());
            }

            hasEdges.put(cur.getId(), hasTo);
            entities.addAll(all);
        } else {
            entities.add(MappingHelper.MapArticle(cur, true));
        }

        /*if (externAll.size() > 0) {
            entities.addAll(externAll);
        }*/
        HashSet<Long> t = new HashSet<>();
        /*for (Entity ent : ext) {
            t.add(ent.getId());
        }*/
        linkEdges.put(cur.getId(), t);

        //getAllRelatedNodes(cur, entities, linkEdges);
        ArborViewModel vm = new ArborViewModel();

        vm.setNodes(entities);
        vm.setLinkEdges(linkEdges);
        vm.setHasEdges(hasEdges);

        return Response.status(200).entity(vm.toJson()).build();
    }

    @GET
    //@QueryParam("subArticle")
    @Path("/articles/{title}")
    @Produces("text/plain")
    public Response getArticles(@PathParam("title") String title) {
        //TODO automatische abfrage während tippen getNodesByTypeAndTitlesequence        
        Iterable<Entity> artObjects = artService.getNodesByTypeAndTitlesequence("Article", ".*" + title + ".*");

        Gson gson = new Gson();

        HashSet<String> allArticles = new HashSet<>();

        for (Entity article : artObjects) {
            allArticles.add(StringHelper.prettyPrintString(article.getTitle()));
        }

        return Response.status(200).entity(gson.toJson(allArticles)).build();
    }

    @GET
    @Path("/checkArticle")
    @Produces("text/plain")
    public Response checkArticle(@QueryParam("title") String title, @QueryParam("mainArticle") String mainArticle, @QueryParam("type") String type) {
        String text = "Shit, that should not happen";
        if (mainArticle == null || mainArticle.equalsIgnoreCase("null")) {
            text = WebCrawler.getArticleText(MediaWikiLanguageHelper.Language.DE, title);
        } else {
            text = WebCrawler.getArticleText(MediaWikiLanguageHelper.Language.DE, mainArticle, title);
        }
        Gson gson = new Gson();

        return Response.status(200).entity(gson.toJson(text)).build();
    }


    private void getAllRelatedNodes(ArticleObject cur, List<EntityViewModel> entities, Hashtable<Long, HashSet<Long>> edges) {
        EntityViewModel current = MappingHelper.MapArticle(cur);
        entities.add(current);
        Iterable<Entity> ext = artService.getRelatedNodes(cur.getTitle());
        List<EntityViewModel> externAll = MappingHelper.MapEntities(ext, cur.getTitle());

        getRelatedNodesForNode(cur.getTitle(), NodeType.ARTICLE, current, entities, edges);
    }

    private void getRelatedNodesForNode(String title, NodeType type, EntityViewModel current, List<EntityViewModel> entities, Hashtable<Long, HashSet<Long>> edges) {
        Iterable<Entity> ext;
        switch (type) {
            case ARTICLE:
                ext = artService.getRelatedNodes(title);
                break;
            case SUBARTICLE:
                ext = subArtService.getRelatedNodes(title);
                break;
            case EXTERN:
                ext = extService.getRelatedNodes(title);
                break;
            default:
                throw new AssertionError(type.name());
        }

        for (Entity extern : ext) {
            EntityViewModel curExtern = MappingHelper.MapEntity(extern);

            if (edges.containsKey(current.getId())) {
                edges.get(current.getId()).add(curExtern.getId());
            } else {
                edges.put(current.getId(), new HashSet<Long>());
                edges.get(current.getId()).add(curExtern.getId());
            }
            entities.add(curExtern);

            getRelatedNodesForNode(extern.getTitle(), getTypeForEntity(extern), curExtern, entities, edges);
        }
    }
}