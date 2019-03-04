import React from 'react';
import Form from 'antd/lib/form';
import Checkbox from 'antd/lib/checkbox';
import { get, concat, compact, compose } from 'lodash/fp';
import cssModules from 'react-css-modules';

import { onPickOrDeliverySelect } from '../actionCreators/shippingInfo'
import { validateAirportInfo } from '../utils/validators'
import ShippingContact from '../contact/ShippingContact'
import AirportSelector from './airportSelector';
import styles from './airport_select.module.scss';

const checkboxTextPortMap = {
  departure: '上门取货',
  arrival: '机场派送',
}

const getAllErrors = (field, localErrors, errors) => {
  return compose(
    get(0),
    compact,
    concat(errors[field]),
  )(localErrors[field])
}

const AirportSelect = ({isPickUpOrDropOff = false, location = 'domestic', airport = {}, ...rest}) => {
  const {dispatch, port, layouts, label, isValidating, changed, errors} = rest
  const localErrors = (isValidating || changed) ? validateAirportInfo({isPickUpOrDropOff, airport, ...rest}) : {}

  const airportErrors = getAllErrors('airport', localErrors, errors)
  const addressErrors = getAllErrors('address', localErrors, errors)

  return (
    <div className={styles.airport_select}>
      <Form.Item
        {...layouts.formItemLayout}
        {...airportErrors}
        label={label}
        required={true}
      >
        <AirportSelector
          {...rest}
          airport={airport}
          location={location}
        />
      </Form.Item>

      <Form.Item
        {...layouts.nonLabelFormItemLayout}
      >
        <Checkbox
          checked={isPickUpOrDropOff}
          onChange={() => dispatch(onPickOrDeliverySelect(port))}
          disabled={!airport.pickup}
        >
          {checkboxTextPortMap[port]}
        </Checkbox>
      </Form.Item>

      {isPickUpOrDropOff && (
        <Form.Item
          {...addressErrors}
          {...layouts.nonLabelFormItemLayout}
        >
          <ShippingContact {...rest} airport={airport}/>
        </Form.Item>
      )}
    </div>
  )
}

export default cssModules(AirportSelect, styles);
