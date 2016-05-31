(function() {
    'use strict';

    angular
        .module('pronostiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('home', {
            parent: 'app',
            url: '/',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/home/home.html',
                    controller: 'HomeController',
                    controllerAs: 'vm'
                }
            }
        })
        .state('homeMatches', {
            parent:'app',
            url:'/',
            data: {
                authorities: []
            },
            views: {
                'content@':{
                    templateUrl:'app/matches/matches.html',
                    controller: 'UserMatchController',
                    controllerAs:'vm'
                }
            }
        })
        .state('pronostic', {
                      parent:'app',
                      url:'/',
                      data: {
                          authorities: ['ROLE_USER']
                      },
                      views: {
                          'content@':{
                              templateUrl:'app/pronostic/pronostics.html',
                              controller: 'PronosticController',
                              controllerAs:'vm'
                          }
                      }
                  })
    }
})();
