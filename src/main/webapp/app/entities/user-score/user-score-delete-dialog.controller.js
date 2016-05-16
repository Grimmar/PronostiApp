(function() {
    'use strict';

    angular
        .module('pronostiApp')
        .controller('UserScoreDeleteController',UserScoreDeleteController);

    UserScoreDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserScore'];

    function UserScoreDeleteController($uibModalInstance, entity, UserScore) {
        var vm = this;
        vm.userScore = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            UserScore.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
