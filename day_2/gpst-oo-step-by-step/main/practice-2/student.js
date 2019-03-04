const Person = require('./person');

module.exports = class Student extends Person{
    constructor(name,age,klass) {
        super(name,age);
        this.klass=klass;
    }
    introduce(){
        return super.introduce()+` I am a Student.`+ (this.klass.leader===this?` I am Leader of Class ${this.klass.number}.`:` I am at Class ${this.klass.number}.`);
    }
} 