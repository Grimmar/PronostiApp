(function() {
    'use strict';
    angular
        .module('pronostiApp')
        .factory('UserScore', UserScore);

    UserScore.$inject = ['$resource'];

    function UserScore ($resource) {
        var resourceUrl =  'api/user-scores/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
