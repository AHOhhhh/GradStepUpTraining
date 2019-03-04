const Person = require('../../main/practice-2/person');
const Student = require('../../main/practice-2/student');
const Teacher = require('../../main/practice-2/teacher');
const Class = require('../../main/practice-2/class');

describe("Person", () => {
  it("should have field name and age", () => {
    let person = new Person("Tom", 21);
    expect(person.name).toBe("Tom");
    expect(person.age).toBe(21);
  });

  it("should have a method introduce, introduce person with name and age", () => {
    let person = new Person("Tom", 21);
    let introduce = person.introduce();
    expect(introduce).toBe("My name is Tom. I am 21 years old.");

  });

  describe("Student", () => {
    it("should have field name, age and class", () => {
      let klass = new Class(2);
      let student = new Student("Tom", 21, klass);
      expect(student.name).toBe("Tom");
      expect(student.age).toBe(21);
      expect(student.klass).toBe(klass);

    });

    it("should overwrite Person introduce, introduce student with class", () => {
      let klass = new Class(2);
      let student = new Student("Tom", 21, klass);
      let introduce = student.introduce();

      expect(introduce).toBe("My name is Tom. I am 21 years old. I am a Student. I am at Class 2.");

    });

    it("should introduce student is a leader, when class assign this student to be a leader",
    () => {
      let klass = new Class(2);
      let student = new Student("Tom", 21, klass);
      klass.assignLeader(student);
      let introduce = student.introduce();

      expect(introduce).toBe("My name is Tom. I am 21 years old. I am a Student. I am Leader of Class 2.");

    });

  });

  describe("Teacher", () => {

    it("should have field name, age and classes", () => {
      let klass1 = new Class(2);
      let klass2 = new Class(3);
      let teacher = new Teacher("Joun", 21, [klass1, klass2]);
      expect(teacher.name).toBe("Joun");
      expect(teacher.age).toBe(21);
      expect(teacher.classes).toEqual([klass1, klass2]);
    });

    it("should overwrite Person introduce and show all classes this teacher teaches",
    () => {
      let klass1 = new Class(2);
      let klass2 = new Class(3);
      let teacher = new Teacher("Joun", 21, [klass1, klass2]);

      let introduce = teacher.introduce();

      expect(introduce).toBe("My name is Joun. I am 21 years old. I am a Teacher. I teach Class 2,3.");

    });

    it("should show no class this teacher teach, when there is no class assgined to this teacher",
    () => {
      let teacher = new Teacher("Joun", 21, []);
      let introduce = teacher.introduce();
      expect(introduce).toBe("My name is Joun. I am 21 years old. I am a Teacher. I teach No Class.");
    });

  });
});
