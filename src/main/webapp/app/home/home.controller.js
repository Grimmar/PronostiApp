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
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();
        getAllTeam();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }

        function getAllTeam ($scope, $state, Team) {
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
    }
})();
