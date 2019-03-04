import React from 'react'
import {Link} from 'react-router'
import {connect} from 'react-redux'
import NAV_ITEMS from './../../config/nav'
import styles from './index.module.scss'

const SideBarNavigation = (props) => (
  <div className={styles.sidebar}>
    <ul>
      {NAV_ITEMS.map((item, index) => {
        return item.role.includes(props.user.role) && (
          <li key={index}>
            <Link
              className={props.location === item.route ? 'active ' + item.className + '-white' : item.className}
              to={item.route}><i/>{item.label}
            </Link>
          </li>)
      })}
    </ul>
  </div>
)

export default connect(
  state => ({
    user: state.auth.user
  })
)(SideBarNavigation, styles)
