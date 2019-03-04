// Write your code here
module.exports = class Class {
    constructor(number) {
        this.number = number;
        this.members = [];
    }

    assignLeader(student) {
        if (this.hasStudent(student)) {
            this.leader = student;
            if (this.teacher) {
                this.teacher.notifyLeaderAssigned(`${student.name} become Leader of Class ${this.number}`);
            }
            return `Assign team leader successfully.`;
        }
        return `It is not one of us.`;
    }

    appendMember(student) {
        this.members.push(student);
        if (this.teacher) {
            this.teacher.notifyStudentAppended(`${student.name} has joined Class ${this.number}`);
        }
    }

    hasStudent(student) {
        return this.members.includes(student);
    }

    notify(teacher) {
        this.teacher = teacher;
    }
}