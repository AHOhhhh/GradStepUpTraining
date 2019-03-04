import React, {Component} from 'react' // eslint-disable-line
import {Select, Cascader} from 'antd';
import {get} from 'lodash'
import cssModules from 'react-css-modules'
import regionCode from './regionCode.json'
import styles from './index.module.scss';

const Option = Select.Option;

class RegionSelector extends Component {

  constructor(props) {
    super(props);
    const value = this.props.value;
    this.state = {
      ...value,
    }
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.value) {
      const value = nextProps.value;
      this.setState({
        ...this.state,
        ...value,
      });
    }
  }

  getRegionCode(country, codes) {
    const obj = codes.find(code => code.country === country);
    if (obj) {
      return obj.code;
    }
    return 'CN'
  }

  onRegionChange(value, selectedOptions = {}) {
    const onChange = this.props.onChange;
    const provinceLevel = 0;
    const cityLevel = 1;
    const districtLevel = 2;
    const newState = {
      checkRegion: true,
      province: get(selectedOptions[provinceLevel], 'label'),
      provinceId: get(selectedOptions[provinceLevel], 'value'),
      city: get(selectedOptions[cityLevel], 'label'),
      cityId: get(selectedOptions[cityLevel], 'value'),
      district: get(selectedOptions[districtLevel], 'label'),
      districtId: get(selectedOptions[districtLevel], 'value'),
    };

    if (onChange) {
      onChange(Object.assign({}, this.state, newState));
    }
  }

  onCountryChange = (value) => {
    const onChange = this.props.onChange;
    const newState = {
      checkRegion: false,
      country: value,
      countryAbbr: this.getRegionCode(value, regionCode),
      province: null,
      provinceId: null,
      city: null,
      cityId: null,
      district: null,
      districtId: null
    };
    if (onChange) {
      onChange(Object.assign({}, this.state, newState));
    }
  };

  render() {
    const {options} = this.props;
    const {country, provinceId, cityId, districtId} = this.state;

    return (
      <div className={styles.cascader}>
        <div className="country">
          <Select
            onChange={::this.onCountryChange}

            placeholder="选择地区"
            value={country}
          >
            <Option value="中国大陆">中国大陆</Option>
            <Option value="台湾">台湾</Option>
            <Option value="香港">香港</Option>
            <Option value="澳门">澳门</Option>
          </Select>
        </div>
        {
          country === '中国大陆' &&
          <div className="region">
            <Cascader
              value={[provinceId, cityId, districtId]}
              options={options}
              onChange={::this.onRegionChange}
              placeholder="选择省、市、区"
              showSearch
            />
          </div>
        }
      </div>
    )
  }
}

export default cssModules(RegionSelector, styles, {allowMultiple: true})
