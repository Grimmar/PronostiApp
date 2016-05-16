(function() {
    'use strict';

    angular
        .module('pronostiApp')
        .controller('MatchDialogController', MatchDialogController);

    MatchDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Match', 'Team'];

    function MatchDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Match, Team) {
        var vm = this;
        vm.match = entity;
        vm.teams = Team.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('pronostiApp:matchUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.match.id !== null) {
                Match.update(vm.match, onSaveSuccess, onSaveError);
            } else {
                Match.save(vm.match, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.matchDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
