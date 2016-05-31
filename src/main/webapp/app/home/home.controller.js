(function() {
    'use strict';

    angular
        .module('pronostiApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$resource'];


    function HomeController ($scope, Principal, LoginService, $state, $resource) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.showTree = false;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();
        getAllTeam();
        getNextMatches();
        getLastMatches();
        getTopUserScores();
        initiateTree();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        function register() {
            $state.go('register');
        }

        function getAllTeam ($scope, $state) {
                vm.teams = [];
                vm.loadAll = function() {

                var resourceUrl =  'api/teams/:id';

                var team = $resource(resourceUrl, {}, {
                                'query': { method: 'GET', isArray: true},
                                'get': {
                                    method: 'GET',
                                    transformResponse: function (data) {
                                        data = angular.fromJson(data);
                                        return data;
                                    }
                                }
                            });

                    team.query(function(result) {
                        vm.teams = result;
                    });
                };

                vm.loadAll();

        }

        function getNextMatches($scope, $state) {
                vm.nextMatches = [];
                vm.loadAll = function() {

                        var resourceUrl =  'api/nextMatches';

                        var match = $resource(resourceUrl, {}, {
                            'query': { method: 'GET', isArray: true},
                            'get': {
                                method: 'GET',
                                transformResponse: function (data) {
                                    data = angular.fromJson(data);
                                    data.matchDate = DateUtils.convertDateTimeFromServer(data.matchDate);
                                    return data;
                                }
                            }
                        });
                    match.query(function(result) {
                        vm.nextMatches = result;
                    });
                };

                vm.loadAll();
        }

        function getLastMatches($scope, $state){
            vm.lastMatches = [];
                vm.loadAll = function() {
                    var resourceUrl =  'api/lastMatches';

                    var match = $resource(resourceUrl, {}, {
                        'query': { method: 'GET', isArray: true},
                        'get': {
                            method: 'GET',
                            transformResponse: function (data) {
                                data = angular.fromJson(data);
                                data.matchDate = DateUtils.convertDateTimeFromServer(data.matchDate);
                                    return data;
                                }
                            }
                    });
                    match.query(function(result) {
                        vm.lastMatches = result;
                    });
                };
            vm.loadAll();
        }

        function getTopUserScores($scope, $state){
            vm.topUserScores = [];
            vm.loadAll = function() {
                var resourceUrl = 'api/topUserScores';

                var score = $resource(resourceUrl, {}, {
                                        'query': { method: 'GET', isArray: true},
                                        'get': {
                                            method: 'GET',
                                            transformResponse: function (data) {
                                                data = angular.fromJson(data);
                                                return data;
                                            }
                                        }
                                    });
                score.query(function(result) {
                    vm.topUserScores = result;
                });
            };
            vm.loadAll();
        }

        function initiateTree($scope, $state){
            vm.eighth = [];
            vm.loadAll = function() {

                var resourceUrl =  'api/eighth';
                var match = $resource(resourceUrl, {}, {
                    'query': { method: 'GET', isArray: true},
                    'get': {
                        method: 'GET',
                        transformResponse: function (data) {
                            data = angular.fromJson(data);
                            data.matchDate = DateUtils.convertDateTimeFromServer(data.matchDate);
                            return data;
                        }
                                        }
                    });
                    match.query(function(result) {
                        vm.eighth = result;
                    });
            };

            vm.loadAll();
            if(vm.eighth.count =! 0){
                vm.showTree = true;
                vm.loadAll = function() {
                    var resourceUrl =  'api/fourth';
                    var match = $resource(resourceUrl, {}, {
                                    'query': { method: 'GET', isArray: true},
                                    'get': {
                                        method: 'GET',
                                        transformResponse: function (data) {
                                            data = angular.fromJson(data);
                                            data.matchDate = DateUtils.convertDateTimeFromServer(data.matchDate);
                                            return data;
                                        }
                                                        }
                                    });
                    match.query(function(result) {
                        vm.fourth = result;
                    });
                };
            }
            vm.loadAll();
        }
    }
})();
