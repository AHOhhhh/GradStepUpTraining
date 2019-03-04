import React from 'react'
import cssModules from 'react-css-modules'
import styles from './new_step.module.scss'

const getWrapperClass = (id, current) => {
  switch (true) {
    case id > current:
      return styles.icon_wrapper;
    case id === current:
      return [styles.icon_wrapper, styles.current].join(' ');
    case id < current:
      return [styles.icon_wrapper, styles.completed].join(' ');
    default:
      return styles.icon_wrapper;
  }
}

const NewStep = ({name, id, current}) => (
  <div className={getWrapperClass(id, current)}>
    <span className={styles.step_icon}>{id >= current && id}</span>
    <span className={styles.step_text}>{name}</span>
  </div>
)

export default cssModules(NewStep, styles);
