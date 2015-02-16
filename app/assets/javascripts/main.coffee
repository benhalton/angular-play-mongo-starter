"use strict"
app = angular.module("angular-play-mongo", ["ngResource", "ngRoute", "ngCookies"])
        .config(["$routeProvider", ($routeProvider) ->
            $routeProvider.when("/",
                templateUrl: "/views/home"
                controller: "HomeCtrl"
                isPublic: true
            ).when("/login",
                templateUrl: "views/login"
                controller: "LoginCtrl"
                isPublic: true
            ).when("/register",
                templateUrl: "views/register"
                controller: "RegisterCtrl"
                isPublic: true
            ).when("/notes",
                templateUrl: "/views/list"
                controller: "ListCtrl"
            ).when("/notes/new/:level",
                templateUrl: "/views/detail"
                controller: "CreateCtrl"
            ).when("/contact",
                templateUrl: "views/contact"
                controller: "ContactCtrl"
                isPublic: true
            ).when("/itwentwrong",
              templateUrl: "/views/itwentwrong"
              isPublic: true
            ).when("/notallowed",
              templateUrl: "/views/notallowed"
              isPublic: true
            ).otherwise(
              redirectTo: "/"
            )

        ]).config(["$locationProvider", ($locationProvider) ->
            $locationProvider.html5Mode(true).hashPrefix "!" # enable the new HTML5 routing and history API
        ]).config(["$httpProvider", ($httpProvider) ->
          $httpProvider.interceptors.push 'responseErrorInterceptor'
        ])

.run(["$rootScope", "$location", "UserService", ($rootScope, $location, UserService) ->
            #register listener to watch route changes
            $rootScope.$on("$routeChangeStart", (event, next, current) ->
                if not UserService.isLoggedIn and not next.isPublic
                  #no logged user, we should be going to #login
                  if (next.templateUrl is "html/home.html")
                      # already going to #login, no redirect needed
                  else
                      UserService.checkCredentials().success((response) ->
                          UserService.isLoggedIn = true
                          UserService.permissions = 8
                      ).error () ->
                        $location.path "/notallowed"
                  )
        ])

#the global controller
app.controller("AppCtrl", ["$scope", "$location", "$window", "UserService", ($scope, $location, $window, UserService) ->
    $scope.go = (path) -> $location.path(path)
    $scope.user = UserService
    $scope.$on('$viewContentLoaded', (event) ->
      $window.ga 'send', 'pageview', {'page': $location.path()}
    )
])

app.controller("HomeCtrl", ["$scope", "$http", "UserService", ($scope, $http, UserService) ->

])

app.controller("ListCtrl", ["$scope", "$http", ($scope, $http) ->
    $http.get('api/notes').success (data) -> $scope.notes = data
])

app.controller("ContactCtrl", ["$scope", ($scope) ->

])

app.controller("LoginCtrl", ["$scope", "UserService", "$timeout", ($scope, UserService, $timeout) ->
   $scope.loginFailure = false

   $scope.$watch "credentials", ((newValue, oldValue) -> $scope.loginFailure = false), true

   $scope.login = (credentialsAreValid) ->
     if credentialsAreValid
       UserService.login $scope.credentials,
         (() ->
           $scope.loginFailure = false
           $timeout () -> $scope.go '/home'
         ),
         () -> $scope.loginFailure = true
])

app.controller("RegisterCtrl", ["$scope", "UserService", ($scope, UserService) ->
  $scope.register = () -> UserService.register $scope.credentials
])

app.controller("CreateCtrl", ["$scope", "$routeParams", "$resource", "$timeout",  ($scope, $routeParams, $resource, $timeout) ->
    $scope.level = $routeParams.level

    $scope.save = () ->
        CreateNote = $resource "/api/notes"
        CreateNote.save $scope.note
        $timeout () -> $scope.go '/notes'
])

app.factory("UserService", ["$http", ($http) ->

    login = (credentials, successCallback, failureCallback) ->

      $http.post("api/login", credentials).success((response) ->
        user.isLoggedIn = true
        user.permissions = 8
        successCallback()
      ).error failureCallback

    logout = () ->
      $http.get("api/logout").success((response) ->
        console.log "logging you out"
        user.isLoggedIn = false
        user.permissions = 0
      )

    register = (credentials) ->
        $http.post("api/register", credentials).success (response) ->
            user.isLoggedIn = true
            user.permissions = 8

    checkCredentials = () -> $http.get "api/checkCredentials"

    user =
        isLoggedIn: false
        permissions: 0
        login: login
        logout: logout
        register: register
        checkCredentials: checkCredentials
])

#register the interceptor as a service
app.factory('responseErrorInterceptor', ($q, $location) ->

   'responseError': (rejection) ->
      if rejection.status is 500
        $location.path("/itwentwrong")
      if rejection.status is 404
        console.log "404"
      $q.reject rejection
)
