Feature: Frame bowling - série standard

  Scenario: Le premier lancer augmente le score de la série
    Given une série standard
    And le lancer sur 10 quilles retourne 4
    When le joueur effectue un lancer
    Then le lancer est accepté
    And le score de la série est 4

  Scenario: Le second lancer augmente le score de la série
    Given une série standard
    And le lancer sur 10 quilles retourne 3
    And le lancer sur 7 quilles retourne 4
    When le joueur effectue un lancer
    And le joueur effectue un lancer
    Then le score de la série est 7

  Scenario: Un strike interdit un second lancer
    Given une série standard
    And le lancer sur 10 quilles retourne 10
    When le joueur effectue un lancer
    And le joueur effectue un lancer
    Then le lancer est refusé
    And le score de la série est 10

  Scenario: Deux lancers standards interdisent un troisième lancer
    Given une série standard
    And le lancer sur 10 quilles retourne 3
    And le lancer sur 7 quilles retourne 4
    When le joueur effectue un lancer
    And le joueur effectue un lancer
    And le joueur effectue un lancer
    Then le lancer est refusé
    And le score de la série est 7
