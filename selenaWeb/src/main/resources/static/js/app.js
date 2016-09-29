var app = angular.module('app', ['ngRoute','ngResource']);
app.config(function($routeProvider){
    $routeProvider
        .when('/users',{
            templateUrl: '/views/users.html',
            controller: 'usersController'
        })
        .when('/roles',{
            templateUrl: '/views/roles.html',
            controller: 'rolesController'
        })
        when('/player',{
            templateUrl: '/views/player.html',
            controller: 'playerController'
        })
        .otherwise(
            {
                redirectTo: '/player'
            }
        );
});