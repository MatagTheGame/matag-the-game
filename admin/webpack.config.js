'use strict'

var path = require('path')

var BUILD_DIR = path.resolve(__dirname, 'src/main/resources/public/js')
var APP_DIR = path.resolve(__dirname, 'src/main/resources/app/src')

module.exports = {
  entry: APP_DIR + '/admin.js',
  output: {
    path: BUILD_DIR,
    filename: 'admin.js'
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        include: APP_DIR,
        loader: 'babel-loader'
      },
      {
        test: /\.s(a|c)ss$/,
        use: [
          // Creates `style` nodes from JS strings
          'style-loader',
          // Translates CSS into CommonJS
          'css-loader',
          // Compiles Sass to CSS
          'sass-loader'
        ]
      }
    ]
  },
  resolve: {
    alias: {
      Main: APP_DIR
    }
  }
}