(function() {
    'use strict';

    angular
        .module('pronostiApp')
        .controller('UserPronosticDetailController', UserPronosticDetailController);

    UserPronosticDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'UserPronostic', 'User', 'Team', 'Match'];

    function UserPronosticDetailController($scope, $rootScope, $stateParams, entity, UserPronostic, User, Team, Match) {
        var vm = this;
        vm.userPronostic = entity;
        
        var unsubscribe = $rootScope.$on('pronostiApp:userPronosticUpdate', function(event, result) {
            vm.userPronostic = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
