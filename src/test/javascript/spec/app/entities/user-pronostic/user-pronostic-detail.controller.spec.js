'use strict';

describe('Controller Tests', function() {

    describe('UserPronostic Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockUserPronostic, MockUser, MockTeam, MockMatch;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockUserPronostic = jasmine.createSpy('MockUserPronostic');
            MockUser = jasmine.createSpy('MockUser');
            MockTeam = jasmine.createSpy('MockTeam');
            MockMatch = jasmine.createSpy('MockMatch');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'UserPronostic': MockUserPronostic,
                'User': MockUser,
                'Team': MockTeam,
                'Match': MockMatch
            };
            createController = function() {
                $injector.get('$controller')("UserPronosticDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pronostiApp:userPronosticUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
