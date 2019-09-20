package com.coxautodev.graphql.tools.example.resolvers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.coxautodev.graphql.tools.example.CharacterRepository;
import com.coxautodev.graphql.tools.example.types.Character;
import com.coxautodev.graphql.tools.example.types.Droid;
import com.coxautodev.graphql.tools.example.types.Episode;
import com.coxautodev.graphql.tools.example.types.Human;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingFieldSelectionSet;

@Component
public class Query implements GraphQLQueryResolver {


    private static final Logger log = LoggerFactory.getLogger(Query.class);


    @Autowired
    private CharacterRepository characterRepository;

    public Character hero(Episode episode) {
        return episode != null ? characterRepository.getHeroes().get(episode) : characterRepository.getCharacters().get("1000");
    }

    public Human human(String id, DataFetchingEnvironment env) {
        return (Human) characterRepository.getCharacters().values().stream()
            .filter(character -> character instanceof Human && character.getId().equals(id))
            .findFirst()
            .orElseGet(null);
    }

    public Droid droid(String id) {
        return (Droid) characterRepository.getCharacters().values().stream()
            .filter(character -> character instanceof Droid && character.getId().equals(id))
            .findFirst()
            .orElseGet(null);
    }

    public Character character(String id, DataFetchingEnvironment dataFetchingEnvironment) {

        DataFetchingFieldSelectionSet selectionSet = dataFetchingEnvironment.getSelectionSet();

        if (selectionSet.contains("name")) {
            log.warn("Why is 'name' found?");
        }

        if (selectionSet.contains("homePlanet")) {
            log.warn("Woo 'homePlanet' found!");
        } else {
            log.warn("But 'homePlanet' NOT found?");
        }

        return this.characterRepository.getCharacters().get(id);
    }
}
