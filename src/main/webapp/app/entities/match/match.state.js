(function() {
    'use strict';

    angular
        .module('pronostiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('match', {
            parent: 'entity',
            url: '/match',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'Matches'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/match/matches.html',
                    controller: 'MatchController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('match-detail', {
            parent: 'entity',
            url: '/match/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'Match'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/match/match-detail.html',
                    controller: 'MatchDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Match', function($stateParams, Match) {
                    return Match.get({id : $stateParams.id});
                }]
            }
        })
        .state('match.new', {
            parent: 'match',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/match/match-dialog.html',
                    controller: 'MatchDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                matchDate: null,
                                diffusion: null,
                                matchType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('match', null, { reload: true });
                }, function() {
                    $state.go('match');
                });
            }]
        })
        .state('match.edit', {
            parent: 'match',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/match/match-dialog.html',
                    controller: 'MatchDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Match', function(Match) {
                            return Match.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('match', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('match.delete', {
            parent: 'match',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/match/match-delete-dialog.html',
                    controller: 'MatchDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Match', function(Match) {
                            return Match.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('match', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
