(function() {
    'use strict';

    angular
        .module('pronostiApp')
        .controller('UserMatchController', UserMatchController);

    UserMatchController.$inject = ['$scope',  '$state', '$resource'];


    function UserMatchController ($scope, $state, $resource) {
        var vm = this;

        getAllMatches();

        function getAllMatches ($scope, $state) {
                vm.matches = [];
                vm.loadAll = function() {

                var resourceUrl =  'api/matchesOrdered';

                var matches = $resource(resourceUrl, {}, {
                                'query': { method: 'GET', isArray: true},
                                'get': {
                                    method: 'GET',
                                    transformResponse: function (data) {
                                        data = angular.fromJson(data);
                                        return data;
                                    }
                                }
                            });

                    matches.query(function(result) {
                        vm.matches = result;
                    });
                };

                vm.loadAll();

        }


    }
})();
