import React from 'react'
import Input from 'antd/lib/input'
import Radio from 'antd/lib/radio'
import Tabs from 'antd/lib/tabs'
import { map, concat, fromPairs } from 'lodash'
import { map as fpMap, mapkeys, filter, keys, toPairs, head, sortBy, groupBy, compose } from 'lodash/fp'

import styles from './airport_list.module.scss';
import {
  changeAirportLocation,
  onAirportSelect,
} from '../actionCreators/shippingInfo'

const filterLocations = (airports, abroad) => {
  const convMapkeys = mapkeys.convert({cap: false})
  const convMap = fpMap.convert({cap: false})

  // 将机场列表转换为以字母排序四个一组的显示用数组
  return compose(
    convMapkeys(value => map(value, v => v[0]).join('')),
    groupBy(airport => airport[2]),
    convMap((ele, index) => concat(ele, Math.floor(index / 4))),
    sortBy(0),
    toPairs,
    groupBy(airport => head(airport.name.toUpperCase())),
    filter({abroad}),
  )(airports)
}

const getTab = ({location, airports, ...rest}) => {
  const locationByAlphabet = filterLocations(airports, (location === 'international'))
  return (
    <Tabs tabPosition={location}>
      {map(keys(locationByAlphabet), (groupedAirportsKey) =>
        (<Tabs.TabPane
          tab={groupedAirportsKey}
          key={groupedAirportsKey}>
          <AirportList
            {...rest}
            airports={locationByAlphabet[groupedAirportsKey]}
          />
        </Tabs.TabPane>)
      )}
    </Tabs>
  )
}

const getItemClassName = (airport = {}, selected = {}) => (
  airport.airportCode === selected.airportCode ? 'list_item selected' : 'list_item'
)

const AirportList = ({airports, airport: selected, port, toggleDropdown, dispatch}) => (
  <div>
    {map(fromPairs(airports), (airportListByFL, key) => (
      <ul className={styles.airport_list} key={`airport-list-${key}`}>
        <div className="list_name">{key}</div>
        {map(airportListByFL, (airport, index) =>
          (<li
            className={getItemClassName(airport, selected)}
            key={`airport-item-${index}`}
          >
            <a
              onClick={() => {
                toggleDropdown()
                dispatch(onAirportSelect(airport, port))
              }}
            >{airport.city}</a>
          </li>))}
      </ul>
    ))}
  </div>
)

const AirportDropdown = (props) => {
  const {airport = {}, dispatch, port, location, placeholder, isOpen, toggleDropdown} = props
  return (<div>
    <Input
      readOnly={true}
      placeholder={placeholder}
      value={airport.airportName || null}
      onClick={toggleDropdown}
    />

    {isOpen && (
      <div className={styles.airport_selector}>
        <Radio.Group
          onChange={(e) => { dispatch(changeAirportLocation(port, e.target.value)) }}
          value={location}
        >
          <Radio.Button value="domestic">国内</Radio.Button>
          <Radio.Button value="international">国际</Radio.Button>
        </Radio.Group>
        {getTab({...props})}
      </div>
    )}
  </div>)
}

export default class AirportSelector extends React.Component {
  state = {
    isOpen: false,
  }

  componentDidMount() {
    document.addEventListener('mousedown', ::this.handleClickOutside);
  }

  componentWillUnmount() {
    document.removeEventListener('mousedown', ::this.handleClickOutside);
  }

  handleClickOutside(e) {
    if (this.wrapperRef && !this.wrapperRef.contains(e.target)) {
      this.closeDropdown()
    }
  }

  closeDropdown() {
    this.setState({
      isOpen: false,
    })
  }

  toggleDropdown() {
    this.setState({
      isOpen: !this.state.isOpen,
    })
  }

  render() {
    return (<div ref={node => { this.wrapperRef = node }}>
      <AirportDropdown
        {...this.props}
        toggleDropdown={::this.toggleDropdown}
        isOpen={this.state.isOpen}
      />
    </div>)
  }
}
