(function() {
    'use strict';
    angular
        .module('pronostiApp')
        .factory('UserPronostic', UserPronostic);

    UserPronostic.$inject = ['$resource', 'DateUtils'];

    function UserPronostic ($resource, DateUtils) {
        var resourceUrl =  'api/user-pronostics/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.pronosticDate = DateUtils.convertDateTimeFromServer(data.pronosticDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
