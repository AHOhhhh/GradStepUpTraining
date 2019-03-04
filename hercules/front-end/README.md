# HNA logistics

## Installation

Run the following commands to start the server

```sh
npm install
npm start
```

The server is listening at ```http://localhost:1337```

## Build a package

```sh
npm run build
```

This will generate some files under folder `dist/`, and it has been ignored of course
in file `.gitignore`.

## Code Diff 时间

 **每周三、周五 16:00**
 
 **Code diff 时一半时间 diff，一半时间分享，并且根据群里人员顺序轮流 diff 或分享**
 
---- 

## 代码规范约定

1. container 中请求后端数据，必须使用 action，reducer 机制<font color=YellowGreen>（确定）</font>

2. 实现业务使用 container，单纯 UI 渲染抽象成 component；如果 container 比较复杂，可以建子文件夹抽象子 container<font color=YellowGreen>（确定）</font>

3. 可在业务文件夹下建立 share 文件夹，放置可共用的 container 和静态资源<font color=YellowGreen>（确定）</font>

4. Table组件如何抽象，是否直接使用 ant design 的 Table 组件，只抽象 Table style<font color=Orange>（待定）</font>

5. 在 components 及 containers 中均使用 cssModules -- 各自修改自己的页面

    **参照：**
   
     ```
     app/containers/WMS/CreateOrderContainer/index.js
     app/containers/WMS/CreateOrderContainer/index.module.css
     ```
  
6. ant-theme.less 中已经覆盖了 ant design 的样式（后续可能需要抽出 themes 文件夹） ，需要改变 ant-design 组件样式的时候<font color="yellowGreen">优先修改 ant-design.less </font>中的样式

7. 组件尽量 stateless

8. 引入 ant design 的component 时只引入特定的一个，例如 import {Input} from 'antd/lib/input'

9. 使用 modal 时，参照 popModalEdit/index.js 、basicModal/index.js 

10. 对于 root reducer， 可以根据业务份文件夹，不要全部写在 root reducer 中

## tasks

1. 各自补单元测试

