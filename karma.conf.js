// Karma configuration
// http://karma-runner.github.io/0.10/config/configuration-file.html

module.exports = function (config) {
    config.set({
        // base path, that will be used to resolve files and exclude
        basePath: '',

        // testing framework to use (jasmine/mocha/qunit/...)
        frameworks: ['jasmine'],

        // list of files / patterns to load in the browser
        files: [
            'public/third/angularjs/angular.min.js',
            'public/third/angularjs/angular-resource.min.js',
            'public/third/angularjs/angular-route.min.js',
            'public/third/angularjs/angular-cookies.min.js',
            'app/assets/javascripts/**/*.coffee',
            'test/javascript/**/*.js',
            'test/javascript/**/*.coffee'
        ],

        // list of files / patterns to exclude
        exclude: [],

        // web server port
        port: 8090,

        // level of logging
        // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
        logLevel: config.LOG_INFO,


        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: false,


        // Start these browsers, currently available:
        // - Chrome
        // - ChromeCanary
        // - Firefox
        // - Opera
        // - Safari (only Mac)
        // - PhantomJS
        // - IE (only Windows)
        browsers: ['PhantomJS'],


        // Continuous Integration mode
        // if true, it capture browsers, run tests and exit
        singleRun: true ,

        reporters: ['progress']


    });
};
