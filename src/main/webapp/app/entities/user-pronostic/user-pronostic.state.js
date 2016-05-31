(function() {
    'use strict';

    angular
        .module('pronostiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-pronostic', {
            parent: 'entity',
            url: '/user-pronostic',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'UserPronostics'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-pronostic/user-pronostics.html',
                    controller: 'UserPronosticController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('user-pronostic-detail', {
            parent: 'entity',
            url: '/user-pronostic/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'UserPronostic'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-pronostic/user-pronostic-detail.html',
                    controller: 'UserPronosticDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'UserPronostic', function($stateParams, UserPronostic) {
                    return UserPronostic.get({id : $stateParams.id});
                }]
            }
        })
        .state('user-pronostic.new', {
            parent: 'user-pronostic',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-pronostic/user-pronostic-dialog.html',
                    controller: 'UserPronosticDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                scoreTeam1: null,
                                scoreTeam2: null,
                                pronosticDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-pronostic', null, { reload: true });
                }, function() {
                    $state.go('user-pronostic');
                });
            }]
        })
        .state('user-pronostic.edit', {
            parent: 'user-pronostic',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-pronostic/user-pronostic-dialog.html',
                    controller: 'UserPronosticDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserPronostic', function(UserPronostic) {
                            return UserPronostic.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-pronostic', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-pronostic.delete', {
            parent: 'user-pronostic',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-pronostic/user-pronostic-delete-dialog.html',
                    controller: 'UserPronosticDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserPronostic', function(UserPronostic) {
                            return UserPronostic.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-pronostic', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
