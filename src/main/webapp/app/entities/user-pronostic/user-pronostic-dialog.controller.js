(function() {
    'use strict';

    angular
        .module('pronostiApp')
        .controller('UserPronosticDialogController', UserPronosticDialogController);

    UserPronosticDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserPronostic', 'User', 'Team', 'Match'];

    function UserPronosticDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserPronostic, User, Team, Match) {
        var vm = this;
        vm.userPronostic = entity;
        vm.users = User.query();
        vm.teams = Team.query();
        vm.matches = Match.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('pronostiApp:userPronosticUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.userPronostic.id !== null) {
                UserPronostic.update(vm.userPronostic, onSaveSuccess, onSaveError);
            } else {
                UserPronostic.save(vm.userPronostic, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.pronosticDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
