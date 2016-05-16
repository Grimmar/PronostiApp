(function() {
    'use strict';

    angular
        .module('pronostiApp')
        .controller('UserScoreController', UserScoreController);

    UserScoreController.$inject = ['$scope', '$state', 'UserScore'];

    function UserScoreController ($scope, $state, UserScore) {
        var vm = this;
        vm.userScores = [];
        vm.loadAll = function() {
            UserScore.query(function(result) {
                vm.userScores = result;
            });
        };

        vm.loadAll();
        
    }
})();
