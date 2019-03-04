import { map, mapValues, extend } from 'lodash'

import provinces from './province.json'
import cities from './city.json'
import areas from './area.json'

const mapRegionToList = (higher, lower) => {
  return map(higher, (item) => {
    return extend({
      value: item.id,
      label: item.name
    }, lower && {children: lower[item.id]})
  })
}

const mapRegionToObject = (higherRegion, lower) => {
  return mapValues(higherRegion, (region) => mapRegionToList(region, lower))
}

const lowerRegions = mapRegionToObject(cities, mapRegionToObject(areas))
const options = mapRegionToList(provinces, lowerRegions)
export default options
