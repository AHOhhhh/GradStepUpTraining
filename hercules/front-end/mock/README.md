详细api文档参见 <http://restifyjs.com/>

这里是一组 mock 数据和 mock 行为，它可以在开发阶段快速建立一个伪服务器。


### 使用:

1. npm install
2. npm start

mock server 会开启本地 `8090` 端口


### 与 brightness-frontend 配合使用

1. `cd HNA-Brightness && npm run dev`, 启动 webpack-dev-server, 会反向代理 `8090` 端口
2. `cd brightness-mock-server && npm start`, 启动 mock-server 服务
