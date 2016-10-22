// js/app.js
'use strict';

var app = angular.module('app', ['ngRoute','ngResource']);
app.config(function($routeProvider){
    $routeProvider
        .when('/player',{
            templateUrl: 'partials/player.html',
            controller: 'playerController'
        })
        .when('/recorder',{
            templateUrl: 'partials/recorder.html',
            controller: 'recorderController'
        })
        .when('/settings',{
            templateUrl: 'partials/settings.html',
            controller: 'settingsController'
        })
        .when('/help',{
            templateUrl: 'partials/help.html',
            controller: 'helpController'
        })
        .otherwise(
            {
                redirectTo: '/player'
            }
        );
});