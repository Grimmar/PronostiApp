(function() {
    'use strict';

    angular
        .module('pronostiApp')
        .controller('UserPronosticController', UserPronosticController);

    UserPronosticController.$inject = ['$scope', '$state', 'UserPronostic'];

    function UserPronosticController ($scope, $state, UserPronostic) {
        var vm = this;
        vm.userPronostics = [];
        vm.loadAll = function() {
            UserPronostic.query(function(result) {
                vm.userPronostics = result;
            });
        };

        vm.loadAll();
        
    }
})();
