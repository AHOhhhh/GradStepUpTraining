const assert = require('assert').strict;
const Fabonacci = require('../src/Fabonacci');
  describe('Fabonacci', () => {
  describe('#of', () => {
    it('should return 1 given 1', () => {
      const actual = Fabonacci.of(1);
      const expect = 1;
      assert.deepEqual(actual, expect);
    });

    it('should return 1 given 2', () => {
      const actual = Fabonacci.of(2);
      const expect = 1;
      assert.deepEqual(actual, expect);
    });

    it('should return 2 given 3', () => {
      const actual = Fabonacci.of(3);
      const expect = 2;
      assert.deepEqual(actual, expect);
    });

    it('should return 3 given 4', () => {
      const actual = Fabonacci.of(4);
      const expect = 3;
      assert.deepEqual(actual, expect);
    });

  });

});