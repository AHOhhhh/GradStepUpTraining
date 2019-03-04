## 该文件对项目的目录结构做出详细解释。
- 总体目录结构用Tree的形式给出，每个节点代表一个目录结构
- 括号（）中是具体解释

<pre><code>

.（项目根目录）
├── app （前端根目录）
│   ├── assets （静态资源）
│   │   └── fonts （ant design icon fonts）
│   ├── src （源码）
│   │   ├── actions （react actions）
│   │   ├── components （项目中所有自行研发的components）
│   │   │   ├── BasicModal （模态窗）
│   │   │   ├── Breadcrumb （面包屑v1）
│   │   │   ├── BreadcrumbV2 （面包屑v2）
│   │   │   ├── CancelOrderButton （取消订单Button）
│   │   │   ├── Footer （页面底部的页脚）
│   │   │   ├── Header （页面顶部的页眉）
│   │   │   ├── ImagePreviewer （图片预览）
│   │   │   ├── ImageUploader （图片上传）
│   │   │   ├── LeftNavigation （左边侧边栏）
│   │   │   ├── PaymentConfirmModal （确认支付弹窗）
│   │   │   ├── PopModal （确认弹窗）
│   │   │   ├── PopModalClear （清空联系人弹窗）
│   │   │   ├── PopModalEdit （编辑联系人弹窗）
│   │   │   ├── PopModalList （联系人列表弹窗）
│   │   │   ├── ProductList （货品列表）
│   │   │   ├── PromptModal （确定对话框）
│   │   │   ├── PromptModalWithCancel （确定／取消对话框）
│   │   │   ├── RHSActionPanel （订单服务完成面板）
│   │   │   ├── Record （操作记录）
│   │   │   ├── RegionCascader
│   │   │   ├── RegionSelector （地区下拉列表）
│   │   │   ├── SSOUploadButton （上传资料Button）
│   │   │   ├── Section （订单页面Section）
│   │   │   ├── Steppers 
│   │   │   │   ├── OrderStepper （订单状态Stepper）
│   │   │   │   ├── SignStepper （注册企业用户Stepper）
│   │   │   │   └── WisePortOrderSteppers （口岸报关订单状态Stepper）
│   │   │   └── wrappedAntComponent （查询框）
│   │   ├── config （环境配置，路由配置）
│   │   ├── constants （用户相关常量，订单相关常量）
│   │   ├── containers （页面文件夹）
│   │   │   ├── ACG （航空货运子业态）
│   │   │   │   ├── CreateOrderContainer
│   │   │   │   ├── OrderManagementContainer
│   │   │   │   ├── PaymentFailureContainer
│   │   │   │   ├── components
│   │   │   │   └── share
│   │   │   ├── Admin （后台管理）
│   │   │   │   ├── EditOrderModal
│   │   │   │   ├── EnterpriseDetailContainer
│   │   │   │   ├── EnterpriseListContainer
│   │   │   │   ├── FundsListContainer
│   │   │   │   ├── LoginContainer
│   │   │   │   ├── OperationOrderBillsContainer
│   │   │   │   ├── OperationRecordsContainer
│   │   │   │   ├── OrderDetailPreviewContainer
│   │   │   │   ├── OrderList
│   │   │   │   ├── PreviewEnterpriseInfo
│   │   │   │   └── share
│   │   │   ├── AppContainer （主页面）
│   │   │   ├── CheckoutCounter （收银台）
│   │   │   │   ├── OfflinePaymentSection
│   │   │   │   ├── PaymentFailureContainer
│   │   │   │   └── PaymentSuccessContainer
│   │   │   ├── MWP （口岸报关子业态）
│   │   │   │   ├── CreateOrderContainer
│   │   │   │   ├── WisePortOrderContainer
│   │   │   │   └── share
│   │   │   ├── NotFound （404页面）
│   │   │   ├── Official （官网集成）
│   │   │   │   └── OrderSearchContainer （运单查询页面）
│   │   │   ├── SCF （供应链金融子业态）
│   │   │   │   ├── CreateSCFOrderContainer
│   │   │   │   ├── SCFOrderContainer
│   │   │   │   └── constants
│   │   │   ├── SSO （单点登录）
│   │   │   │   └── AuthorizeContainer
│   │   │   ├── User （用户模块）
│   │   │   │   ├── ChangePasswordContainer
│   │   │   │   ├── ContactInfoContainer
│   │   │   │   ├── EditUserModal
│   │   │   │   ├── EnterpriseInfoContainer
│   │   │   │   ├── EnterpriseStatusManager
│   │   │   │   ├── LandingPageContainer
│   │   │   │   ├── LoginContainer
│   │   │   │   ├── LogoutContainer
│   │   │   │   ├── OrderListContainer
│   │   │   │   ├── SignUpContainer
│   │   │   │   ├── SignUpEnterpriseContainer
│   │   │   │   ├── SignUpTermsContainer
│   │   │   │   ├── SignupSucceedContainer
│   │   │   │   └── UploadMaterialContainer
│   │   │   ├── WMS （WMS子业态）
│   │   │   │   ├── CreateOrderContainer
│   │   │   │   ├── OrderCancelledContainer
│   │   │   │   ├── OrderContainer
│   │   │   │   ├── OrderDetailsContainer
│   │   │   │   ├── PaymentContainer
│   │   │   │   ├── PaymentResultContainer
│   │   │   │   ├── PriceVerificationContainer
│   │   │   │   └── share
│   │   │   └── shared
│   │   │       ├── AccountBasicInfo
│   │   │       ├── AuditPaymentButton
│   │   │       ├── Contact
│   │   │       ├── EnterpriseQualificationInfo
│   │   │       ├── FundInfo
│   │   │       ├── GoodList
│   │   │       ├── ModalFooterButtons
│   │   │       ├── OrderDetailStatus
│   │   │       ├── PaymentFailure
│   │   │       ├── ProductModal
│   │   │       ├── ReferenceOrderContainer
│   │   │       └── assets
│   │   ├── polyfills （浏览器兼容性）
│   │   └── utils （工具，中间件）
│   └── styles （全局样式）
├── bin （CI相关）
├── config （测试配置）
├── dist （Build后的存储目录）
├── docs （文档）
├── mock （前端Mock Server）
├── nginx_conf （nginx配置文件）
├── pacts （契约测试）
├── .babelrc （Babel配置文件）
├── .browserslistrc （支持的浏览器配置文件）
├── .editorconfig （空格风格配置文件）
├── .eslintignore （eslint检查忽略项）
├── .eslintrc.js （eslint检查配置文件）
├── .gitattributes （git配置项）
├── .gitignore （git跟踪忽略项）
├── Dockerfile  （Dockerfile）
├── package.json 
├── package-lock.json
├── postcss.config.js （postcss配置文件）
├── README.md
├── webpack.config.js （开发版本的webpack配置文件）
└── webpack.config.prod.js （生产版本的webpack配置文件）

</code></pre>
