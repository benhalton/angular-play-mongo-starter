#!/bin/bash

BASE_DIR=`dirname $0`

cd $BASE_DIR
npm install
cd -

echo ""
echo "Starting Karma Server (http://karma-runner.github.io)"
echo "-------------------------------------------------------------------"

export "PHANTOMJS_BIN=$BASE_DIR/node_modules/.bin/phantomjs"

$BASE_DIR/node_modules/.bin/karma start $BASE_DIR/karma.conf.js $*
