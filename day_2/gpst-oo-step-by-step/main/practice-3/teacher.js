const Person = require('./person');

module.exports = class Teacher extends Person {
  constructor(name, age, clazzes) {
    super(name, age);
    this.clazzes = clazzes;
    this.clazzes.forEach(element => {
      element.notify(this);
    });
  }
  introduce() {
    return super.introduce() + ` I am a Teacher. ` + (this.clazzes.length == 0 ? `I teach No Class.` : `I teach Class ${this.clazzes.map(ele => ele.number).join(',')}.`);
  }
  isTeaching(student) {
    if (!student.clazz.hasStudent(student)) {
      return false;
    }
    let clazz = student.clazz.number;
    let result = this.clazzes.find(ele => ele.number === clazz);
    return !!result;
  }

  notifyStudentAppended(msg) {
    console.log(msg);
  }
  notifyLeaderAssigned(msg) {
    console.log(msg);
  }
}
