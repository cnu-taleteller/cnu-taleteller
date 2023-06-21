const path = require('path');
const dotenv = require('dotenv');
dotenv.config({ path: path.join(__dirname, '.env') });

module.exports = {
  // devServer: {
  //   proxy: {
  //     '/api': {
  //       target: 'http://localhost:8100',
  //       changeOrigin: true,
  //     }
  //   }
  // },
  lintOnSave: false
};