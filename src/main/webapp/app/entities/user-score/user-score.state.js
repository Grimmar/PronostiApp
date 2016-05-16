(function() {
    'use strict';

    angular
        .module('pronostiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-score', {
            parent: 'entity',
            url: '/user-score',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'UserScores'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-score/user-scores.html',
                    controller: 'UserScoreController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('user-score-detail', {
            parent: 'entity',
            url: '/user-score/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'UserScore'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-score/user-score-detail.html',
                    controller: 'UserScoreDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'UserScore', function($stateParams, UserScore) {
                    return UserScore.get({id : $stateParams.id});
                }]
            }
        })
        .state('user-score.new', {
            parent: 'user-score',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-score/user-score-dialog.html',
                    controller: 'UserScoreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                score: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-score', null, { reload: true });
                }, function() {
                    $state.go('user-score');
                });
            }]
        })
        .state('user-score.edit', {
            parent: 'user-score',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-score/user-score-dialog.html',
                    controller: 'UserScoreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserScore', function(UserScore) {
                            return UserScore.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-score', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-score.delete', {
            parent: 'user-score',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-score/user-score-delete-dialog.html',
                    controller: 'UserScoreDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserScore', function(UserScore) {
                            return UserScore.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-score', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
