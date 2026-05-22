package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RechercheVille {
   private List<String> villes;
   
   public List<String> Rechercher(String mot) throws NotFoundException {
      if ("*".equals(mot)) {
          return new ArrayList<>(villes);
      }
      if (mot == null || mot.length() < 2) {
          throw new NotFoundException();
      }
      String recherche = mot.toLowerCase();
      List<String> resultat = new ArrayList<>();
      for (String ville : villes) {
          if (ville.toLowerCase().contains(recherche)) {
              resultat.add(ville);
          }
      }
      return resultat;
  }

   public RechercheVille() {
       villes = Arrays.asList(
               "Paris", "Budapest", "Skopje", "Rotterdam", "Valence", "Vancouver",
               "Amsterdam", "Vienne", "Sydney", "New York", "Londres", "Bangkok",
               "Hong Kong", "Dubaï", "Rome", "Istanbul"
       );
   }
}