describe 'Controller: ListCtrl', () ->

    beforeEach module 'wset-sat-app'

    [ListCtrl, scope, $httpBackend] = []

    beforeEach inject ($controller, $rootScope, _$httpBackend_) ->
        @addMatchers
            toEqualData: (expected) -> angular.equals(@actual, expected)

        scope = $rootScope.$new()
        $httpBackend = _$httpBackend_
        ListCtrl = $controller 'ListCtrl', {$scope: scope}

    it 'should load all notes', () ->
        expectedData = [{"_id": {"$oid": "52e66e200100000100ee9c25"}, "wine": {"name": "foo", "vintage": "1990"}, "note": "notey notey very notey"}]
        $httpBackend.expectGET("api/notes").respond expectedData

        expect(scope.notes).toBeUndefined()
        $httpBackend.flush()
        expect(scope.notes.length).toEqual 1
        expect(scope.notes).toEqualData expectedData