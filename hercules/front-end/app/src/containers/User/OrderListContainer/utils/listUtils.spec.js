import test from 'ava';

import { mapOrderListWithReferenceOrder } from './listUtils'

const originData = [{
  id: 1,
}, {
  id: 2,
  referenceOrder: {
    id: 10,
  }
}, {
  id: 3
}, {
  id: 4,
  referenceOrder: {
    id: 5
  }
}, {
  id: 5,
  referenceOrder: {
    id: 4
  }
}]

test('mapOrderListWithReferenceOrder should return list with referenced orders', t => {
  const expected = [{
    id: 1,
  }, {
    id: 2,
    isReferenced: true,
    referenceOrder: {
      id: 10,
    }
  }, {
    id: 10
  }, {
    id: 3
  }, {
    id: 4,
    isReferenced: true,
    referenceOrder: {
      id: 5
    }
  }, {
    id: 5
  }, {
    id: 5,
    isReferenced: true,
    referenceOrder: {
      id: 4
    }
  }, {
    id: 4
  }]
  const actual = mapOrderListWithReferenceOrder(originData)
  t.deepEqual(actual, expected)
});
