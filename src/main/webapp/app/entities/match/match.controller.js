(function() {
    'use strict';

    angular
        .module('pronostiApp')
        .controller('MatchController', MatchController);

    MatchController.$inject = ['$scope', '$state', 'Match'];

    function MatchController ($scope, $state, Match) {
        var vm = this;
        vm.matches = [];
        vm.loadAll = function() {
            Match.query(function(result) {
                vm.matches = result;
            });
        };

        vm.loadAll();
        
    }
})();
