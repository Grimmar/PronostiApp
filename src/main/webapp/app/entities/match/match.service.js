(function() {
    'use strict';
    angular
        .module('pronostiApp')
        .factory('Match', Match);

    Match.$inject = ['$resource', 'DateUtils'];

    function Match ($resource, DateUtils) {
        var resourceUrl =  'api/matches/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.matchDate = DateUtils.convertDateTimeFromServer(data.matchDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
