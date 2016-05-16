(function() {
    'use strict';

    angular
        .module('pronostiApp')
        .controller('MatchDetailController', MatchDetailController);

    MatchDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Match', 'Team'];

    function MatchDetailController($scope, $rootScope, $stateParams, entity, Match, Team) {
        var vm = this;
        vm.match = entity;
        
        var unsubscribe = $rootScope.$on('pronostiApp:matchUpdate', function(event, result) {
            vm.match = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
