import React, {Component} from 'react'; // eslint-disable-line
import cssModules from 'react-css-modules';
import {Link} from 'react-router';
import styles from './index.module.scss';
import userTerm from './assets/user_term.docx';
import platformTerm from './assets/platform_term.docx';

class TermsContainer extends Component { // eslint-disable-line react/prefer-stateless-function

  render() {
    return (
      <div styleName="terms-container">
        <div styleName="terms">
          <h2 styleName="head-title">Hercules平台服务条款</h2>
          <div styleName="content">
            <p>欢迎“您”（以下或称“用户”）与“Hercules科技有限公司”（下称“Hercules”）共同签署本《Hercules服务条款》协议（下称“本协议”），本协议阐述之条款和条件适用于您使用Hercules基于互联网，以包含Hercules平台、客户端等在内的各种形态（包括未来技术发展出现的新的服务形态）向您提供的各项服务。</p>
            <br/>

            <section>
              <h3>一、接受协议</h3>
              <ul>
                <li>1.1本协议内容包括协议正文及所有Hercules已经发布或将来可能发布的各类规则。所有规则为协议不可分割的一部分，与协议正文具有同等法律效力</li>
                <li>1.2您在点击同意本协议之前，应当认真阅读本协议。<span styleName="underline-content">请您务必审慎阅读、充分理解各条款内容，特别是免除或者限制责任的条款、法律适用和争议解决条款。免除或者限制责任的条款将以粗体下划线标识，您应重点阅读。</span>如您对协议有任何疑问，向Hercules客服咨询。</li>
                <li>1.3当您按照注册页面提示填写信息、阅读并同意本协议且完成全部注册程序，或以任何方式进入Hercules平台并使用服务即表示您已充分阅读、理解并同意接受本协议的条款和条件。<span styleName="underline-content">阅读本协议的过程中，如果您不同意本协议或其中任何条款约定，您应立即停止注册程序并停止使用服务。</span>
                </li>
              </ul>
            </section>

            <br/>
            <section>
              <h3>二、定义</h3>
              <ul>
                <li><span styleName="bold-content">2.1Hercules平台：</span>指Hercules主办运营的面向物流企业的在线信息发布及交易平台，包含Hercules中国网站（所涉域名为 haipingx.com，下同）及客户端。</li>
                <li><span styleName="bold-content">2.2Hercules：</span>指“Hercules科技有限公司”，系Hercules平台的经营者。</li>
                <li><span styleName="bold-content">2.3Hercules服务：</span>指Hercules基于互联网，以包含Hercules中国网站、客户端等在内的各种形态（包括未来技术发展出现的新的服务形态）向您提供的各项服务（下称“服务”）。</li>
                <li><span styleName="bold-content">2.4Hercules平台：</span>指名称为Hercules（域名为haipingx.com）的网站及客户端。</li>
                <li><span styleName="bold-content">2.5关联公司：</span>指除Hercules外Hercules平台的经营者的单称或合称。</li>
                <li><span styleName="bold-content">2.6 Hercules平台规则：</span>包括在所有Hercules平台规则频道内已经发布及后续发布的全部规则、解读、公告等内容以及各平台在论坛、帮助中心内发布的各类规则、实施细则、产品流程说明、公告等。</li>
              </ul>
            </section>

            <br/>
            <section>
              <h3>三、协议范围</h3>
              <ul>
                <li>
                  <p styleName="bold-content">3.1签约主体</p>
                  本协议由您与平台经营者共同缔结，本协议对您与Hercules具有同等合同效力。Hercules平台经营者可能根据Hercules平台的业务调整而发生变更，变更后的Hercules经营者与您共同履行本协议并向您提供服务，Hercules平台经营者的变更不会影响您本协议项下的权益。Hercules平台经营者还有可能因为提供新的Hercules平台服务而新增，如您使用新增的Hercules平台服务，视为您同意新增的Hercules经营者与您共同履行本协议。发生争议时，您可根据您具体使用的服务及对您权益产生影响的具体行为对象确定与您履约的主体及争议对方。
                </li>
                <li>
                  <p styleName="bold-content">3.2补充协议</p>
                  由于互联网的高速发展，您与Hercules签署的本协议列明的条款并不能完整罗列并覆盖您与Hercules所有权利与义务，现有的约定也不能保证完全未来发展的需求。因此，Hercules平台法律申明及隐私权政策、Hercules平台规则均为本协议的补充协议，与本协议不可分割且具有同等法律效力。如您使用Hercules平台服务，是为您同意上述补充协议。
                </li>
              </ul>
            </section>

            <br/>
            <section>
              <h3>四、账户注册与使用</h3>
              <ul>
                <li>
                  <p styleName="bold-content">4.1用户资格</p>
                  <ul>
                    <li>4.1.1您确认，在您完成注册程序或以其他Hercules允许的方式实际使用服务时，您应当具备中华人民共和国法律规定的与您行为相适应的民事行为能力。<span styleName="underline-content">若您不具备前述与您行为相适应的民事行为能力，则您及您的监护人应依照法律规定承担因此而导致的一切后果，且Hercules有权终止向您提供服务并注销您的账户。</span>如您代表一家公司或其他法律主体在Hercules平台注册，则您声明和保证，您有权使该公司或该法律主体受本协议的约束。</li>
                    <li>4.1.2此外，您还需确保您不是任何国家、国际组织或者地域实施的贸易限制、制裁或其他法律、规则限制的对象，否则您可能无法正常注册及使用服务。</li>
                  </ul>
                </li>
                <li>
                  <p styleName="bold-content">4.2账户注册</p>
                  <ui>
                    <l2>4.2.1在您按照注册页面提示填写信息、阅读并同意本协议且完成全部注册程序后，或在您按照激活页面提示填写信息、阅读并同意本协议且完成全部激活程序后，或您以其他Hercules允许的方式实际使用Hercules服务时，您即受本协议的约束。您可以使用您提供或确认的邮箱、手机号码或者Hercules允许的其它方式作为登录手段进入Hercules平台。</l2>
                    <l2>4.2.2由于您的账户关联您的信息及Hercules平台商业信息，您的账户仅限您本人使用。未经Hercules同意，您直接或间接授权第三方使用您账户或获取您账户项下信息的行为无效。如Hercules根据您账户的使用行为等情况判断您账户的使用可能危及您的账户安全及/或Hercules平台信息安全的，Hercules可拒绝提供相应服务或终止本协议。</l2>
                  </ui>
                </li>
                <li>
                  <p styleName="bold-content">4.3账户转让</p>
                  由于用户账户关联用户信用信息，仅当有法律明文规定、司法裁定或经Hercules同意，并符合Hercules平台规则规定的账户转让流程的情况下，您可进行账户的转让。您的账户一经转让，该账户项下权利义务一并转移。<span styleName="underline-content">除此外，您的账户不得以任何方式转让，否则Hercules有权追究您的违约责任，且由此产生的一切责任均由您承担</span>
                </li>
                <li>
                  <p styleName="bold-content">4.4实名认证</p>
                  为使您更好地使用Hercules平台的各项服务，保障您的账户安全，Hercules可要求您按国家法律法规的规定完成实名认证。
                </li>
                <li>
                  <p styleName="bold-content">4.5不活跃账户回收</p>
                  <p styleName="underline-content">如您的账户同时符合以下条件，不能再登陆Hercules平台，所有Hercules平台服务将同时终止：</p>
                  <ul>
                    <li styleName="underline-content">（一）连续6个月未使用您的邮箱、手机或Hercules认可的其他方式和密码登录过Hercules平台，也未登录过其他任一Hercules平台；</li>
                    <li styleName="underline-content">（二）不存在未到期的有效业务</li>
                  </ul>
                </li>
                <li>
                  <p styleName="bold-content">4.6注册信息管理</p>
                  <ul>
                    <li>4.6.1在完成注册或激活流程时，您应当按Hercules平台相应页面的提示准确完整地提供并及时更新您的信息，以使之真实、及时，完整和准确。在国家法律法规有明确规定要求Hercules作为平台服务提供者必须对用户（如航空货运公司等）的信息进行核实的情况下，Hercules将依法不时地对您的信息进行检查核实，您应当配合提供最新、真实、完整、有效的信息。</li>
                    <li>4.6.2 <span styleName="underline-content"> 如Hercules按您最后一次提供的信息与您联系未果、您未按Hercules的要求及时提供信息、您提供的信息存在明显不实或行政司法机关核实您提供的信息无效的，您将承担因此对您自身、他人及Hercules造成的全部损失与不利后果。Hercules可向您发出询问或要求整改的通知，并要求您进行重新认证，直至中止、终止对您提供部分或全部Hercules服务，Hercules对此不承担责任。</span></li>
                    <li styleName="underline-content">4.6.3<span styleName="underline-content">您设置的账户登录名及昵称不得违反国家法律法规及Hercules平台规则的相应管理规定，否则Hercules可回收您的登录名及昵称，并按Hercules平台规则进行相应的处理。</span></li>
                  </ul>
                </li>
                <li>
                  <p styleName="bold-content">4.7账户安全</p>
                  <ul>
                    <li>4.7.1您的账户为您自行设置并由您保管，Hercules任何时候均不会主动要求您提供您的账户密码。因此，建议您务必保管好您的账户，并确保您在每个上网时段结束时退出登录并以正确步骤离开Hercules平台。<span styleName="underline-content">账户因您主动泄露或因您遭受他人攻击、诈骗等行为导致的损失及后果，Hercules并不承担责任，您应通过司法、行政等救济途径向侵权行为人追偿。</span></li>
                    <li>4.7.2除Hercules存在过错外，<span styleName="underline-content">您应对您账户项下的所有行为结果（包括但不限于在线签署各类协议、发布信息、询/报价、采购产品、订购服务及披露信息等）负责。</span></li>
                    <li>4.7.3如发现任何未经授权使用您账户登录Hercules平台或其他可能导致您账户遭窃、遗失的情况，建议您立即通知Hercules。<span styleName="underline-content">您理解Hercules对您的任何请求采取行动均需要合理时间，且Hercules应您请求而采取的行动可能无法避免或阻止侵害后果的形成或扩大，除Hercules存在法定过错外，Hercules不承担责任。</span></li>
                  </ul>
                </li>
              </ul>
            </section>

            <br/>
            <section>
              <h3>五、服务及规范</h3>
              <p>您有权在Hercules平台上享受账户信息管理、产品及/或服务的询价、采购与评价、交易争议处理等服务。Hercules提供的服务内容众多，具体您可登录Hercules平台浏览。</p>
              <ul>
                <li>
                  <p styleName="bold-content">5.1账户管理</p>
                  <ul>
                    <li>5.1.1通过在Hercules平台对您的账户信息进行变更、续费、申请销户操作等。</li>
                    <li>5.1.2 如您需要关停账户，则您应当对您账户下已经达成的交易继续承担全部的的交易保障责任。</li>
                  </ul>
                </li>
                <li>
                  <p styleName="bold-content">5.2产品及/或服务的报价、销售与推广</p>
                  <ul>
                    <li>5.2.1通过Hercules提供的服务，您有权通过文字、图片等形式在Hercules平台上发布产品及/或服务信息、进行报价、招揽和物色交易对象、达成交易。</li>
                    <li>
                      <p>5.2.2您应当确保您对您在Hercules平台上发布的产品及/或服务享有相应的权利，<span styleName="underline-content">您不得在Hercules平台上销售以下产品及/或提供以下服务：</span></p>
                      <p styleName="underline-content">（一）国家禁止或限制的；</p>
                      <p styleName="underline-content">（二）侵犯他人知识产权或其它合法权益的；</p>
                      <p styleName="underline-content">（三）Hercules平台规则、公告、通知或各平台与您单独签署的协议中已明确说明不适合在Hercules平台上销售及/或提供的。</p>
                    </li>
                    <li>5.2.3您应当遵守诚实信用原则，确保您所发布的产品及/或服务信息真实、与您实际所销售的产品及/或提供的服务相符，并在交易过程中切实履行您的交易承诺。您应当维护Hercules平台良性的市场竞争秩序，不得贬低、诋毁竞争对手，不得干扰Hercules平台上进行的任何交易、活动，不得以任何不正当方式提升或试图提升自身的信用度，不得以任何方式干扰或试图干扰Hercules平台的正常运作。</li>
                    <li>5.2.4您有权自行决定产品及/或服务的促销及推广方式，Hercules亦为您提供了形式丰富的促销推广工具。<span styleName="underline-content">您的促销推广行为应当符合国家相关法律法规及Hercules平台的要求。</span></li>
                    <li>5.2.5<span styleName="underline-content">依法纳税是每一个公民、企业应尽的义务，您应对销售额/营业额超过法定免征额部分及时、足额地向税务主管机关申报纳税。</span></li>
                  </ul>
                </li>
                <li>
                  <p styleName="bold-content">5.3服务的询价、采购</p>
                  <ul>
                    <li>5.3.1当您在Hercules平台询价、采购服务时，请您务必仔细确认采购服务详细信息或服务的时间、内容、限制性要求等重要事项，并在下单时核实您的联系地址、电话、收货人等信息。<span styleName="underline-content">如您填写的收货人非您本人，则该收货人的行为和意思表示产生的法律后果均由您承担。</span></li>
                    <li>5.3.2<span styleName="underline-content">您充分了解并同意，Hercules平台是一个物流服务平台，而并非面向消费者的消费购买市场，故您的询价、采购行为应当基于真实的业务需求，不得存在对产品及/或服务实施恶意询价、采购、恶意维权等扰乱Hercules平台正常交易秩序的行为。基于维护Hercules平台交易秩序及交易安全的需要，Hercules发现上述情形时可主动执行关闭相关交易订单等操作。</span></li>
                  </ul>
                </li>
                <li>
                  <p styleName="bold-content">5.4交易争议处理</p>
                  <ul>
                    <li>5.4.1 您可通过Hercules平台所支持的交易方式与其他用户达成交易，并遵守相应交易与支付规则。您理解并同意，<span styleName="underline-content">Hercules平台仅作为用户物色交易对象，就产品和服务的交易进行协商，以及获取各类与贸易相关的服务的地点，Hercules不会参与用户间的交易，亦不会涉及用户间因交易而产生的法律关系及法律纠纷。</span></li>
                    <li>
                      <p>5.4.2您在Hercules平台交易过程中与其他用户发生争议的，您或其他用户中任何一方均有权选择以下途径解决：</p>
                      <ul>
                        <li>（一）与争议相对方自主协商；</li>
                        <li>（二）使用Hercules平台提供的争议调处服务；</li>
                        <li>（三）向有关行政部门投诉；</li>
                        <li>（四）根据与争议相对方达成的仲裁协议（如有）提请仲裁机构仲裁；</li>
                        <li>（五）向人民法院提起诉讼。</li>
                      </ul>
                    </li>
                    <li>5.4.3如您依据Hercules平台规则使用Hercules平台的争议调处服务，则表示您认可并愿意履行Hercules平台的客服作为独立的第三方根据其所了解到的争议事实并依据Hercules平台规则所作出的调处决定（包括调整相关订单的交易状态、判定将争议款项的全部或部分支付给交易一方或双方、执行相应的保证金赔付等）。在Hercules平台调处决定作出前，您可选择上述其他争议处理途径解决争议以中止Hercules平台的争议调处服务。<span styleName="underline-content">如您对调处决定不满意，您仍有权采取其他争议处理途径解决争议，但通过其他争议处理途径未取得终局决定前，您仍应先履行调处决定。</span></li>
                  </ul>
                </li>
              </ul>
            </section>

            <br/>
            <section>
              <h3>六、费用</h3>
              <ul>
                <li>6.1Hercules为Hercules平台向您提供的服务付出了大量的成本，除Hercules平台明示的收费业务外，Hercules向您提供的服务目前是免费的。如未来Hercules向您收取合理费用，Hercules会采取合理途径并提前通过法定程序以本协议约定的有效方式通知您，确保您有充分选择的权利。</li>
                <li>6.2您因进行交易、向Hercules获取有偿服务或接触Hercules服务器而发生的所有应纳税赋，以及相关硬件、软件、通讯、网络服务及其他方面的费用均由您自行承担。</li>
              </ul>
            </section>

            <br/>
            <section>
              <h3>七、责任限制</h3>
              <ul>
                <li>
                  <p>7.1<span styleName="underline-content">Hercules依照法律规定履行基础保障义务，但对于下述原因导致的合同履行障碍、履行瑕疵、履行延后或履行内容变更等情形，Hercules并不承担相应的违约责任：</span></p>
                  <ul>
                    <li styleName="underline-content">（一）因自然灾害、罢工、暴乱、战争、政府行为、司法行政命令等不可抗力因素；</li>
                    <li styleName="underline-content">（二）因电力供应故障、通讯网络故障等公共服务因素或第三人因素；</li>
                    <li styleName="underline-content">（三）在Hercules已尽善意管理的情况下，因常规或紧急的设备与系统维护、设备与系统故障、网络信息与数据安全等因素。</li>
                  </ul>
                </li>
                <li>7.2Hercules仅向您提供本协议约定之服务，您了解Hercules平台上的信息系用户自行发布，且可能存在风险和瑕疵。Hercules将通过依照法律规定建立相关检查监控制度尽可能保障您在Hercules平台的合法权益及良好体验。同时，<span styleName="underline-content">鉴于Hercules平台具备存在海量信息及信息网络环境下信息与实物相分离的特点，Hercules无法逐一审查产品及/或服务的信息，无法逐一审查交易所涉及的产品及/或服务的质量、安全以及合法性、真实性、准确性，对此您应谨慎判断。</span></li>
                <li>7.3您理解并同意，在争议调处服务中，Hercules平台的客服并非专业人士，仅能以普通人的认知对用户提交的凭证进行判断，<span styleName="underline-content">Hercules不保证争议调处决定一定符合您的期望，除存在故意或重大过失外，Hercules对争议调处决定免责。</span></li>
              </ul>
            </section>

            <br/>
            <section>
              <h3>八、用户信息的保护及授权</h3>
              <ul>
                <li>
                  <p styleName="bold-content">8.1个人信息的保护</p>
                  <p>Hercules非常重视用户个人信息（即能够独立或与其他信息结合后识别用户身份的信息）的保护，在您使用Hercules提供的服务时，您同意Hercules按照在Hercules平台上公布的隐私政策收集、存储、使用、披露和保护您的个人信息。Hercules希望通过隐私政策向您清楚地介绍Hercules对您个人信息的处理方式，因此Hercules建议您完整地阅读隐私政策（点击<a href={userTerm} styleName="blue-link" download="用户协议及隐私条款">此处</a>或点击Hercules平台首页底部链接），以帮助您更好地保护您的隐私权。</p>

                </li>
                <li>
                  <p styleName="bold-content">8.2非个人信息的保证与授权</p>
                  <ul>
                    <li>8.2.1您声明并保证，您对您所发布的信息拥有相应、合法的权利。否则，<span styleName="underline-content">Hercules可对您发布的信息依法或依本协议进行删除或屏蔽。</span></li>
                    <li>
                      <p>8.2.2<span styleName="underline-content">您应当确保您所发布的信息不包含以下内容：</span></p>
                      <ul>
                        <li styleName="underline-content">（一）违反国家法律法规禁止性规定的；</li>
                        <li styleName="underline-content">（二）政治宣传、封建迷信、淫秽、色情、赌博、暴力、恐怖或者教唆犯罪的；</li>
                        <li styleName="underline-content">（三）欺诈、虚假、不准确或存在误导性的；</li>
                        <li styleName="underline-content">（四）侵犯他人知识产权或涉及第三方商业秘密及其他专有权利的；</li>
                        <li styleName="underline-content">（五）侮辱、诽谤、恐吓、涉及他人隐私等侵害他人合法权益的；</li>
                        <li styleName="underline-content">（六）存在可能破坏、篡改、删除、影响Hercules平台任何系统正常运行或未经授权秘密获取Hercules平台及其他用户的数据、个人资料的病毒、木马、爬虫等恶意软件、程序代码的；</li>
                        <li styleName="underline-content">（七）其他违背社会公共利益或公共道德或依据相关Hercules平台协议、规则的规定不适合在Hercules平台上发布的。</li>
                      </ul>
                    </li>
                    <li>8.2.3对于您提供、发布及在使用Hercules服务中形成的除个人信息外的文字、图片、视频、音频等非个人信息，<span styleName="underline-content">在法律规定的保护期限内您免费授予Hercules及其关联公司获得全球排他的许可使用权利及再授权给其他第三方使用并可以自身名义对第三方侵权行为取证及提起诉讼的权利。您同意Hercules及其关联公司存储、使用、复制、修订、编辑、发布、展示、翻译、分发您的非个人信息或制作其派生作品，并以已知或日后开发的形式、媒体或技术将上述信息纳入其它作品内。</span></li>
                    <li>8.2.4为方便您使用Hercules平台等其他相关服务，<span styleName="underline-content">您授权Hercules将您在账户注册和使用Hercules服务过程中提供、形成的信息传递给Hercules平台等其他相关服务提供者，或从Hercules平台等其他相关服务提供者获取您在注册、使用相关服务期间提供、形成的信息。</span></li>
                  </ul>
                </li>
              </ul>
            </section>

            <br/>
            <section>
              <h3>九、用户的违约及处理</h3>
              <ul>
                <li>
                  <p styleName="bold-content">9.1违约认定</p>
                  <p>发生如下情形之一的，视为您违约：</p>
                  <ul>
                    <li>（一）使用Hercules服务时违反有关法律法规规定的；</li>
                    <li>（二）违反本协议或本协议补充协议（即本协议第3.2条）约定的。</li>
                  </ul>
                  <p styleName="underline-content">为适应电子商务发展和满足海量用户对高效优质服务的需求，您理解并同意，Hercules可在Hercules平台规则中约定违约认定的程序和标准。如：Hercules可依据您的用户数据与海量用户数据的关系来认定您是否构成违约；您有义务对您的数据异常现象进行充分举证和合理解释，否则将被认定为违约。</p>
                </li>
                <li>
                  <p styleName="bold-content">9.2违约处理措施</p>
                  <ul>
                    <li>9.2.1您在Hercules平台上发布的信息构成违约的，<span styleName="underline-content">Hercules可根据相应规则立即对相应信息进行删除、屏蔽处理或对您的服务进行下架、删除、监管。</span></li>
                    <li>9.2.2您在Hercules平台上实施的行为，或虽未在Hercules平台上实施但对Hercules平台及其用户产生影响的行为构成违约的，<span styleName="underline-content">Hercules可依据相应规则对您执行账户扣分、限制参加营销活动、中止向您提供部分或全部服务、划扣违约金等处理措施。如您的行为构成根本违约的，Hercules可关闭您的账户，终止向您提供服务。</span></li>
                    <li>9.2.3当您违约的同时存在欺诈、售假、盗用他人账户等特定情形或您存在危及他人交易安全或账户安全风险时，<span styleName="underline-content">Hercules会依照您行为的风险程度指示支付公司对您的（及您账户绑定的，下同）资金账户采取取消收款、资金止付等强制措施。</span></li>
                    <li>9.2.4<span styleName="underline-content">Hercules可将对您上述违约行为处理措施信息以及其他经国家行政或司法机关生效法律文书确认的违法信息在Hercules平台上予以公示。</span></li>
                  </ul>
                </li>
                <li>
                  <p styleName="bold-content">9.3赔偿责任</p>
                  <ul>
                    <li>9.3.1<span styleName="underline-content">如您的行为使Hercules及/或其关联公司遭受损失（包括自身的直接经济损失、商誉损失及对外支付的赔偿金、和解款、律师费、诉讼费等间接经济损失），您应赔偿Hercules及/或其关联公司的上述全部损失。</span></li>
                    <li>9.3.2<span styleName="underline-content">如您的行为使Hercules及/或其关联公司遭受第三人主张权利，Hercules及/或其关联公司可在对第三人承担金钱给付等义务后就全部损失向您追偿。</span></li>
                    <li>9.3.3<span styleName="underline-content">如因您的行为使得第三人遭受损失或您怠于履行调处决定、Hercules及/或其关联公司出于保护社会公共利益或保护其他用户合法权益目的，可自您的资金账户中划扣相应款项进行支付。</span></li>
                    <li>9.3.4<span styleName="underline-content">您同意Hercules指示支付公司自您的资金账户中划扣相应款项支付上述赔偿款项。如您资金账户中的款项不足以支付上述赔偿款项的，Hercules及/或关联公司可直接抵减您在Hercules及/或其关联公司其它协议项下的款项及/或权益，并可继续追偿。</span></li>
                  </ul>
                </li>
                <li>
                  <p styleName="bold-content">9.4特别约定</p>
                  <ul>
                    <li>9.4.1如您向Hercules及/或其关联公司的雇员或顾问等提供实物、现金、现金等价物、劳务、旅游等价值明显超出正常商务洽谈范畴的利益，则可视为您存在商业贿赂行为。<span styleName="underline-content">发生上述情形的，Hercules可按照Hercules平台规则进行相应处理，并立即终止与您的所有合作并向您收取违约金及/或赔偿金，该等金额以Hercules因您的贿赂行为而遭受的经济损失和商誉损失作为计算依据。</span></li>
                    <li>9.4.2<span styleName="underline-content">如您因严重违约导致Hercules终止本协议时，出于维护平台秩序及保护其他用户合法权益的目的，Hercules及/或其关联公司可对与您在其他协议项下的合作采取中止甚或终止协议的措施，并以本协议约定的有效方式通知您。</span></li>
                    <li>9.4.3<span styleName="underline-content">如Hercules与您签署的其他协议及Hercules及/或其关联公司与您签署的协议中明确约定了对您在本协议项下合作进行关联处理的情形，则Hercules出于维护平台秩序及保护其他用户合法权益的目的，可在收到指令时中止甚至终止协议，并以本协议约定的有效方式通知您。</span></li>
                  </ul>
                </li>
              </ul>
            </section>

            <br/>
            <section>
              <h3>十、协议的变更</h3>
              <ul>
                <li>10.1Hercules可根据国家法律法规变化及维护市场秩序、保护用户合法权益需要，不时修改本协议、补充协议，变更后的协议、补充协议（下称“变更事项”）将通过法定程序并以本协议约定的有效通知方式通知您。</li>
                <li>10.2如您不同意变更事项，您有权于变更事项确定的生效日前联系Hercules反馈意见。如反馈意见得以采纳，Hercules将酌情调整变更事项。<span styleName="underline-content">如您对已生效的变更事项仍不同意的，您应当于变更事项确定的生效之日起停止使用Hercules服务，变更事项对您不产生效力；如您在变更事项生效后仍继续使用Hercules服务，则视为您同意已生效的变更事项。</span></li>
              </ul>
            </section>

            <br/>
            <section>
              <h3>十一、有效通知</h3>
              <ul>
                <li>
                  <p styleName="bold-content">11.1有效联系方式</p>
                  <p>您在注册成为Hercules平台用户，并接受Hercules服务时，您应该向Hercules提供真实有效的联系方式（包括您的电子邮件地址、联系电话、联系地址等），对于联系方式发生变更的，您有义务及时更新有关信息，并保持可被有效联系的状态。</p>
                  <p>您在注册Hercules平台用户时生成的用于登陆Hercules平台接收站内信、系统消息等即时信息的会员账户（包括子账户），也作为您的有效联系方式。</p>
                  <p styleName="underline-content">Hercules将向您的上述联系方式的其中之一或其中若干向您送达各类通知，而此类通知的内容可能对您的权利义务产生重大的有利或不利影响，请您务必及时关注。</p>
                  <p>您有权通过您注册时填写的手机号码或者电子邮箱获取您感兴趣的产品/服务广告信息、促销优惠等商业性信息；<span styleName="underline-content">您如果不愿意接收此类信息，您有权通过Hercules提供的相应的退订功能进行退订。</span></p>
                </li>
                <li>
                  <p styleName="bold-content">11.2通知的送达</p>
                  <ul>
                    <li>11.2.1Hercules通过上述联系方式向您发出通知，其中以电子的方式发出的书面通知，包括但不限于在Hercules平台公告，向您提供的联系电话发送手机短信，向您提供的电子邮件地址发送电子邮件，向您的账户发送系统消息以及站内信信息，在发送成功后即视为送达；以纸质载体发出的书面通知，按照提供联系地址交邮后的第五个自然日即视为送达。</li>
                    <li>
                      <p>11.2.2对于在Hercules平台上因交易活动引起的任何纠纷，您同意司法机关（包括但不限于人民法院）可以通过手机短信、电子邮件等现代通讯方式或邮寄方式向您送达法律文书（包括但不限于诉讼文书）。您指定接收法律文书的手机号码、电子邮箱等联系方式为您在Hercules平台注册、更新时提供的手机号码、电子邮箱联系方式，司法机关向上述联系方式发出法律文书即视为送达。您指定的邮寄地址为您的法定联系地址或您提供的有效联系地址。</p>
                      <p>您同意司法机关可采取以上一种或多种送达方式向您达法律文书，司法机关采取多种方式向您送达法律文书，送达时间以上述送达方式中最先送达的为准。</p>
                      <p>您同意上述送达方式适用于各个司法程序阶段。如进入诉讼程序的，包括但不限于一审、二审、再审、执行以及督促程序等。</p>
                      <p>你应当保证所提供的联系方式是准确、有效的，并进行实时更新。如果因提供的联系方式不确切，或不及时告知变更后的联系方式，使法律文书无法送达或未及时送达，由您自行承担由此可能产生的法律后果。</p>
                    </li>
                  </ul>
                </li>
              </ul>
            </section>

            <br/>
            <section>
              <h3>十二、协议终止 </h3>
              <ul>
                <li>
                  <p styleName="bold-content">12.1协议终止的情形</p>
                  <ul>
                    <li>
                      <p>12.1.1您有权通过以下任一方式终止本协议：</p>
                      <ul>
                        <li>（一）在满足Hercules平台要求的账户注销条件时您通过网站自助服务注销您的账户的；</li>
                        <li>（二）变更事项生效前您停止使用Hercules服务并明示不愿接受变更事项的；</li>
                        <li>（三）您明示不愿继续使用Hercules服务，且符合Hercules平台要求的终止条件的。</li>
                      </ul>
                    </li>
                    <li>
                      <p>12.1.2出现以下情况时，Hercules可以本协议约定的有效方式通知您终止本协议：</p>
                      <ul>
                        <li>（一）您的实名认证身份无效、不再合法存续或无法有效核实；</li>
                        <li>（二）您违反本协议约定，Hercules依据违约条款终止本协议的；</li>
                        <li>（三）您盗用他人账户、发布违禁信息、欺诈、售假、扰乱市场秩序、采取不正当手段牟利等行为，Hercules依据Hercules平台规则对您的账户予以关闭的；</li>
                        <li>（四）除上述情形外，因您多次违反Hercules平台规则相关规定且情节严重，Hercules依据Hercules平台规则对您的账户予以关闭的；</li>
                        <li>（五）您的账户被Hercules依据本协议回收的；</li>
                        <li>（六）您Hercules平台有欺诈、发布或销售假冒伪劣/侵权产品、侵犯他人合法权益或其他严重违法违约行为的；</li>
                        <li>（七）Hercules基于合理理由相信您的行为可能会使您、Hercules及Hercules平台用户等相关方发生严重损害或产生法律责任；</li>
                        <li>（八）其它应当终止服务的情况。</li>
                      </ul>
                    </li>
                  </ul>

                </li>
                <li>
                  <p styleName="bold-content">12.2协议终止后的处理</p>
                  <ul>
                    <li>12.2.1<span styleName="underline-content">本协议终止后，除法律有明确规定外，Hercules无义务向您或您指定的第三方披露您账户中的任何信息。</span></li>
                    <li>12.2.2本协议终止后，Hercules仍享有下列权利：</li>
                    <ul>
                      <li>（一）继续保存您留存于Hercules平台的本协议第八条所列的各类信息；</li>
                      <li>（二）对于您过往的违约行为，Hercules仍可依据本协议向您追究违约责任。</li>
                    </ul>
                    <li>12.2.3<span styleName="underline-content">本协议终止后，对于您在本协议存续期间产生的交易订单，Hercules可通知交易相对方并根据交易相对方的意愿决定是否关闭该等交易订单；如交易相对方要求继续履行的，则您应当就该等交易订单继续履行本协议及交易订单的约定，并承担因此产生的任何损失或增加的任何费用。</span></li>
                  </ul>
                </li>
              </ul>
            </section>

            <br/>
            <section>
              <h3>十三、链接</h3>
              <p>Hercules平台或第三者均可提供与其他万维网网站或资源的链接。由于Hercules并不控制该等网站和资源，您理解并同意，<span styleName="underline-content">Hercules并不对该等外在网站或资源的可用性负责，且不认可该等网站或资源上或可从该等网站或资源获取的任何内容、宣传、产品、服务或其他材料，也不对其等负责或承担任何责任。您进一步理解和同意，对于任何因使用或信赖从此类网站或资源上获取的此类内容、宣传、产品、服务或其他材料而造成（或声称造成）的任何直接或间接损失，Hercules均不承担责任。</span></p>
            </section>

            <br/>
            <section>
              <h3>十四、法律适用、管辖及其他</h3>
              <ul>
                <li>14.1<span styleName="underline-content">本协议之效力、解释、变更、执行与争议解决均适用中华人民共和国大陆地区法律；如无相关法律规定的，则应参照商业惯例和（或）行业惯例。</span></li>
                <li>14.2您与Hercules仅为独立订约人关系。本协议无意结成或创设任何代理、合伙、合营、雇佣与被雇佣或特性授权与被授权关系。</li>
                <li>14.3<span styleName="underline-content">您同意Hercules因经营业务需要有权将本协议项下的权利义务就部分或全部进行转让，并采取合理途径提前通过法定程序以本协议约定的有效方式通知您。</span></li>
                <li>14.4<span styleName="underline-content">因本协议或Hercules服务所引起或与其有关的任何争议，由Hercules与您协商解决。协商不成时，任何一方均可向Hercules平台北京市东城区人民法院提起诉讼。</span></li>
                <li>14.5倘若本协议任何条款被裁定为无效或不可强制执行，该项条款应被撤销，而其余条款应予遵守和执行。条款标题仅为方便参阅而设，并不以任何方式界定、限制、解释或描述该条款的范围或限度。Hercules未就您或其他人士的某项违约行为采取行动，并不表明Hercules撤回就任何继后或类似的违约事件采取动的权利。</li>
              </ul>
            </section>

          </div>
          <div styleName="download-link">
            <div styleName="line" />
            下载<a href={platformTerm} download="平台服务条款">《平台服务条款》</a>和
            <a href={userTerm} download="用户协议及隐私条款">《用户协议及隐私条款》</a>
          </div>
          <div styleName="confirm-buttons">
            <Link to="/login"><button type="button" className="button" styleName="button">不同意</button></Link>
            <Link to="/signup"><button type="button" className="button primary" styleName="button">同意并继续</button></Link>
          </div>
        </div>
      </div>
    );
  }
}

export default cssModules(TermsContainer, styles);