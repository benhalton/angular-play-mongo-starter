'use strict';

describe('Controller: ListCtrl', function () {

    beforeEach(module('wset-sat-app'));

    var ListCtrl, scope, $httpBackend, expectedData;

    beforeEach(inject(function ($controller, $rootScope, _$httpBackend_) {
        this.addMatchers({
            toEqualData: function (expected) {
                return angular.equals(this.actual, expected);
            }
        });
        scope = $rootScope.$new();
        $httpBackend = _$httpBackend_;
        ListCtrl = $controller('ListCtrl', {$scope: scope});

    }));

    it('should load all notes', function () {
        expectedData = [{"_id": {"$oid": "52e66e200100000100ee9c25"}, "wine": {"name": "foo", "vintage": "1990"}, "note": "notey notey very notey"}];
        $httpBackend.expectGET("api/notes").respond(expectedData);

        expect(scope.notes).toBeUndefined();
        $httpBackend.flush();
        expect(scope.notes.length).toEqual(1)
        expect(scope.notes).toEqualData(expectedData)
    });
});
