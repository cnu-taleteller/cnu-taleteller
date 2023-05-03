module.exports = {
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8100',
        changeOrigin: true,
      }
    }
  },
  lintOnSave: false
};