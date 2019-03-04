import React from 'react'
import cssModules from 'react-css-modules'
import {size} from 'lodash'
import styles from './index.module.scss'
import MessageItem from '../share/MessageItem'
import NoContent from '../share/NoContent'

const isDisplayShowMoreTips = (data, currentSize) => {
  return data.totalElements > currentSize
}

const MessageList = ({type, data, handleShowMore, currentSize}) => {
  if (!data || size(data.content) <= 0) {
    return (
      <NoContent />
    )
  }

  return (
    <div>
      {data.content.map((message, index) => (<MessageItem key={index} info={message} type={type}/>))}
      {isDisplayShowMoreTips(data, currentSize) ?
        <div styleName="show-more" onClick={() => handleShowMore(type)}>查看更多</div> :
        <div styleName="show-more no-more">已无更多</div>
      }
    </div>
  )
}

export default cssModules(MessageList, styles, {allowMultiple: true})
