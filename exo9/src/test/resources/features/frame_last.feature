Feature: Frame bowling - série finale

  Scenario: Le second lancer après un strike augmente le score
    Given une série finale
    And le lancer sur 10 quilles retourne 10 puis 5
    When le joueur effectue un lancer
    And le joueur effectue un lancer
    Then le lancer est accepté
    And le score de la série est 15

  Scenario: Le second lancer est accepté après un strike
    Given une série finale
    And le lancer sur 10 quilles retourne 10 puis 3
    When le joueur effectue un lancer
    And le joueur effectue un lancer
    Then le lancer est accepté

  Scenario: Le troisième lancer est accepté après un strike
    Given une série finale
    And le lancer sur 10 quilles retourne 10 puis 5 puis 2
    When le joueur effectue un lancer
    And le joueur effectue un lancer
    And le joueur effectue un lancer
    Then le lancer est accepté

  Scenario: Le troisième lancer après un strike augmente le score
    Given une série finale
    And le lancer sur 10 quilles retourne 10 puis 5 puis 2
    When le joueur effectue un lancer
    And le joueur effectue un lancer
    And le joueur effectue un lancer
    Then le lancer est accepté
    And le score de la série est 17

  Scenario: Le troisième lancer est accepté après un spare
    Given une série finale
    And le lancer sur 10 quilles retourne 7
    And le lancer sur 3 quilles retourne 3
    When le joueur effectue un lancer
    And le joueur effectue un lancer
    And le joueur effectue un lancer
    Then le lancer est accepté

  Scenario: Le troisième lancer après un spare augmente le score
    Given une série finale
    And le lancer sur 10 quilles retourne 7 puis 6
    And le lancer sur 3 quilles retourne 3
    When le joueur effectue un lancer
    And le joueur effectue un lancer
    And le joueur effectue un lancer
    Then le lancer est accepté
    And le score de la série est 16

  Scenario: Sans strike ni spare le troisième lancer est refusé
    Given une série finale
    And le lancer sur 10 quilles retourne 3
    And le lancer sur 7 quilles retourne 4
    When le joueur effectue un lancer
    And le joueur effectue un lancer
    And le joueur effectue un lancer
    Then le lancer est refusé
    And le score de la série est 7

  Scenario: Un quatrième lancer est refusé en série finale
    Given une série finale
    And le lancer sur 10 quilles retourne 10 puis 5 puis 2
    When le joueur effectue un lancer
    And le joueur effectue un lancer
    And le joueur effectue un lancer
    And le joueur effectue un lancer
    Then le lancer est refusé
    And le score de la série est 17
