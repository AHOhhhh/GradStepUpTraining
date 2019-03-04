'use strict'
class Animal {
	constructor(name, legNum, color) {
		this.name = name;
		this.legNum = legNum;
		this.color = color;
	}
	describe() {
		console.log(`My name is ${this.name}, I have ${this.legNum} legs,and my color is ${this.color}`);
	}
}

class Dog extends Animal {
	constructor(name, color) {
		super(name, 4, color);
	}
}

class Fish extends Animal {
	constructor(name, color) {
		super(name, 0, color);
	}
	describe() {
		super.describe();
		console.log("And I can swim");
	}
}

// --------------
const wc = new Animal("wc", 4, 'yellow');
wc.describe();

const dog = new Dog('dog', 'black');
dog.describe();

const fish = new Fish("fish", "red");
fish.describe();

class Dog extends Animal {
	constructor(name, legsNum, color) {
		super(name, legsNum);
		this.color = color;
	}
	yell() {
		console.log(`I am ${name}, I have ${this.legsNumber} legs. My color is ${this.color}`);
	}
}

