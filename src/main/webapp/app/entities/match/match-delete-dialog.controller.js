(function() {
    'use strict';

    angular
        .module('pronostiApp')
        .controller('MatchDeleteController',MatchDeleteController);

    MatchDeleteController.$inject = ['$uibModalInstance', 'entity', 'Match'];

    function MatchDeleteController($uibModalInstance, entity, Match) {
        var vm = this;
        vm.match = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Match.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
