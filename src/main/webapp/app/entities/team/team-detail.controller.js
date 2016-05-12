(function() {
    'use strict';

    angular
        .module('pronostiApp')
        .controller('TeamDetailController', TeamDetailController);

    TeamDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Team'];

    function TeamDetailController($scope, $rootScope, $stateParams, entity, Team) {
        var vm = this;
        vm.team = entity;
        
        var unsubscribe = $rootScope.$on('pronostiApp:teamUpdate', function(event, result) {
            vm.team = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
