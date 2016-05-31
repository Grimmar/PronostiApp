(function() {
    'use strict';

    angular
        .module('pronostiApp')
        .controller('UserPronosticDeleteController',UserPronosticDeleteController);

    UserPronosticDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserPronostic'];

    function UserPronosticDeleteController($uibModalInstance, entity, UserPronostic) {
        var vm = this;
        vm.userPronostic = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            UserPronostic.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
