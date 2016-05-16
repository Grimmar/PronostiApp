(function() {
    'use strict';

    angular
        .module('pronostiApp')
        .controller('UserScoreDialogController', UserScoreDialogController);

    UserScoreDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserScore', 'User'];

    function UserScoreDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserScore, User) {
        var vm = this;
        vm.userScore = entity;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('pronostiApp:userScoreUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.userScore.id !== null) {
                UserScore.update(vm.userScore, onSaveSuccess, onSaveError);
            } else {
                UserScore.save(vm.userScore, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
