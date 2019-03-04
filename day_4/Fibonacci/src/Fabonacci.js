class Fabonacci {
  static of(number) {
    if (number <= 2) {
      return 1;
    }
    return Fabonacci.of(number - 2) + Fabonacci.of(number - 1);
  }
}

module.exports = Fabonacci; 