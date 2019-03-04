const Person = require('./person');

module.exports = class Teacher   extends Person{
    constructor(name,age,classes) {
        super(name,age);
        this.classes=classes;
    }
    introduce(){
        return super.introduce()+` I am a Teacher. `+(this.classes.length==0?`I teach No Class.`:`I teach Class ${this.classes.map(ele=>ele.number).join(',')}.`);
    }
}