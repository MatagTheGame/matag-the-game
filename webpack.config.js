'use strict';

var path = require('path');

var BUILD_DIR = path.resolve(__dirname, 'src/main/resources/public/js');
var APP_DIR = path.resolve(__dirname, 'src/main/resources/app/src');

module.exports = {
  entry: APP_DIR + '/index.js',
  output: {
    path: BUILD_DIR,
    filename: 'mtg.js'
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        include: APP_DIR,
        loader: 'babel-loader'
      },
      {
        test: /\.sass$/,
        use: [
          { loader: "sass-loader" },
        ]
      }
    ]
  }
};