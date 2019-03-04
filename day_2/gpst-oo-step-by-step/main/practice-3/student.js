const Person = require('./person');

module.exports = class Student extends Person {
    constructor(name,age,clazz) {
        super(name,age);
        this.clazz=clazz;
    }
    introduce(){
        return super.introduce()+` I am a Student. `+ (this.clazz.hasStudent(this)?(this.clazz.leader===this?`I am Leader of Class ${this.clazz.number}.`:`I am at Class ${this.clazz.number}.`):`I haven't been allowed to join Class.`);
    }
}