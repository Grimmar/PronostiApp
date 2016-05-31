(function() {
    'use strict';

    angular
        .module('pronostiApp')
        .controller('PronosticController', PronosticController);

    PronosticController.$inject = ['$scope', 'Principal', 'LoginService', '$state',
    '$resource', 'UserPronostic', 'AlertService'];


    function PronosticController ($scope, Principal, LoginService, $state, $resource, UserPronostic, AlertService) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        //vm.login = LoginService.open;
        //vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();
        getAllNextMatchs();
        //getAllLastMatchs();

        function getAccount() {
                    Principal.identity().then(function(account) {
                        vm.account = account;
                        vm.isAuthenticated = Principal.isAuthenticated;
                    });
                }

        function getAllNextMatchs(){
            vm.nextMatches = [];
            vm.loadAll = function() {
            var resourceUrl =  'api/allNextMatches';
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

        vm.save = function(matchId) {

            var currentMatch = $.grep(vm.nextMatches, function(e){ return e.id == matchId; });
            var currentProno = currentMatch[0].userPronosticForCurrentUser;

            currentProno.match = currentMatch[0];

            if(currentProno.scoreTeam1 == null || currentProno.scoreTeam2 == null){
                AlertService.error("Les scores ne peuvent être null");
                return;
            }

            if(currentProno.scoreTeam1 === "" || currentProno.scoreTeam2 === ""){
                AlertService.error("Les scores ne peuvent être null");
                return;
            }

            if(isNaN(currentProno.scoreTeam1) || isNaN(currentProno.scoreTeam2)){
                AlertService.error("NON ! Pas de lettres STP.");
                return;
            }


            delete currentProno.match["userPronosticForCurrentUser"];

            if (currentProno.id !== null) {
                UserPronostic.update(currentProno, onSaveSuccess, onSaveError);
            } else {
                UserPronostic.save(currentProno, onSaveSuccess, onSaveError);
            }

        }

        var onSaveSuccess = function (result) {
            //alert("Success !");
            AlertService.success("Pronostic enregistré !");
            var currentMatch = $.grep(vm.nextMatches, function(e){ return e.id == result.match.id; });
            currentMatch[0]["userPronosticForCurrentUser"] = result;
            console.log(currentMatch);
        };

        var onSaveError = function () {
            AlertService.error("Une erreur est survenue, veuillez contacter l'administrateur !");

        };
    }
})();
