import React from 'react'
import cssModules from 'react-css-modules'
import {includes, isEmpty} from 'lodash'
import {USER_ROLE} from 'constants'
import { Link } from 'react-router';
import styles from './index.module.scss'
import { ORDER_STATUS } from '../../../constants/steps'

const generateLink = item => (
  <Link
    target={item.openNewTab ? '_blank' : '_self'}
    to={item.link}
    rel={item.openNewTab ? 'noopener noreferrer' : ''}
  >{item.text}</Link>)

export const OrderAction = ({user, order: {status, supplementaryFiles, ssoLoginUrl = null}}) => {
  const ActionsMap = [{
    key: 'reOrder',
    text: '重新下单',
    link: '/scf/create_order',
    status: [ORDER_STATUS.orderRejected]
  }, {
    key: 'businessSystem',
    text: '进入业务系统',
    link: ssoLoginUrl,
    openNewTab: true,
    status: [ORDER_STATUS.orderAccepted, ORDER_STATUS.waitForUploadSupplementaryFiles, ORDER_STATUS.waitForFinancier, ORDER_STATUS.waitForFinancierOffer]
  }]

  const getActions = status => ActionsMap.filter(item => includes(item.status, status))
  const currentActions = getActions(status)
  const isPlatformAdmin = user && user.role === USER_ROLE.platformAdmin

  if (!isEmpty(currentActions)) {
    return (
      (status === ORDER_STATUS.orderRejected && isPlatformAdmin) ? '' :
      <div>
        {currentActions.map(item => (
          <div styleName={item.subTitle ? 'action-container' : 'pure-container'} key={item.key}>
            {item.link && generateLink(item)}
            {item.subTitle && <p>{item.subTitle}</p>}
          </div>
        ))}

        {(status === ORDER_STATUS.waitForUploadSupplementaryFiles && supplementaryFiles && supplementaryFiles.rejected && !isPlatformAdmin) ?
          (<div styleName="action-container">
            <Link
              target={'_self'}
              to={'/scf/create_order'}
              rel={'noopener noreferrer'}
            >重新下单</Link>
          </div>) : ''}
      </div>
    )
  }
  return null
}

export default cssModules(OrderAction, styles, {allowMultiple: true})
