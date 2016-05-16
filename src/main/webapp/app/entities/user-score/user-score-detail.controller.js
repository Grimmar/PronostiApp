(function() {
    'use strict';

    angular
        .module('pronostiApp')
        .controller('UserScoreDetailController', UserScoreDetailController);

    UserScoreDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'UserScore', 'User'];

    function UserScoreDetailController($scope, $rootScope, $stateParams, entity, UserScore, User) {
        var vm = this;
        vm.userScore = entity;
        
        var unsubscribe = $rootScope.$on('pronostiApp:userScoreUpdate', function(event, result) {
            vm.userScore = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
