import React from 'react';
import Input from 'antd/lib/input';
import styles from './search.module.scss';

const AntSearch = Input.Search

const Search = (props) => (
  <AntSearch
    className={styles.search}
    {...props}
  />
)

export default Search
